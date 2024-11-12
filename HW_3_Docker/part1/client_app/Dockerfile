FROM python:3.10.12

RUN pip install --no-cache-dir flask requests

COPY ./app /app
WORKDIR /app

CMD ["python3", "client.py"]
