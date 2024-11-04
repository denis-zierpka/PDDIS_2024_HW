#!/bin/bash

LOG_DIR="./disk_monitor"
INTERVAL=30

check_os() {
    if [[ "$OSTYPE" == "linux"* ]]; then
        echo "Linux OS - confirmed"
    else
        echo "Unsupported OS"
        exit 1
    fi
}

stop_by_pid() {
    PID=$(cat "${LOG_DIR}/disk_monitor.pid")
    if ps -p $PID > /dev/null; then
        kill $PID
        echo "Disk monitor stopped - [pid]: $PID"
    else
        echo "Process not found - [pid]: $PID"
    fi
    rm "${LOG_DIR}/disk_monitor.pid"
}

start_monitor() {
    if [ -f "${LOG_DIR}/disk_monitor.pid" ]; then
        stop_by_pid
    fi

    mkdir -p "$LOG_DIR" 

    TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
    LOG_FILE="${LOG_DIR}/disk_monitor_${TIMESTAMP}.csv"
    check_os
    
    echo "Timestamp,Filesystem,Disk_Size,Disk_Used,Disk_Usage(%),IUsed,IFree" > "$LOG_FILE"
    
    (
        while true; do
            CURRENT_DATE=$(date +"%Y%m%d")
            CURRENT_FULL_TIME=$(date +"%Y-%m-%d %H:%M:%S") 
            LOG_FILE_DATE=$(basename "$LOG_FILE" | cut -d'.' -f1 | cut -d'_' -f3)
            
            if [[ "$CURRENT_DATE" != "$LOG_FILE_DATE" ]]; then
                TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
                LOG_FILE="${LOG_DIR}/disk_monitor_${TIMESTAMP}.csv"
                echo "Timestamp,Filesystem,Disk_Size,Disk_Used,Disk_Usage(%),IUsed,IFree" > "$LOG_FILE"
            fi
            
            paste <(df -h) <(df -i) | awk 'NR>1 {print $1 "," $2 "," $3 "," $5 "," $9 "," $10}' | 
                while IFS=',' read -r filesystem d_size d_used d_usage i_used i_free; do
                    echo "${CURRENT_FULL_TIME},${filesystem},${d_size},${d_used},${d_usage},${i_used},${i_free}" >> "$LOG_FILE"
            done

            sleep $INTERVAL 
        done
    ) &

    echo $! > "${LOG_DIR}/disk_monitor.pid"
    echo "Disk monitor started - [pid]: $!"
}

stop_monitor() {
    if [ -f "${LOG_DIR}/disk_monitor.pid" ]; then
        stop_by_pid
    else
        echo "Disk monitor is not running"
    fi
}

status_monitor() {
    if [ -f "${LOG_DIR}/disk_monitor.pid" ]; then
        PID=$(cat "${LOG_DIR}/disk_monitor.pid")
        if ps -p $PID > /dev/null; then
            echo "Disk monitor is running - [pid]: $PID"
        else
            echo "Process not found - [pid]: $PID"
        fi
    else
        echo "Disk monitor is not running"
    fi
}


case "$1" in
    START)
        start_monitor
        ;;
    STOP)
        stop_monitor
        ;;
    STATUS)
        status_monitor
        ;;
    *)
        echo "usage: $0 {START|STOP|STATUS}"
        exit 1
        ;;
esac
