FROM python:3.10.12

RUN pip install --no-cache-dir flask

COPY ./src /src
WORKDIR /src

CMD ["python3", "server.py"]
