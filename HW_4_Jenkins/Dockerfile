FROM python:3.10.12

RUN pip install --no-cache-dir flask

COPY ./app /app
WORKDIR /app

CMD ["python3", "server.py"]
