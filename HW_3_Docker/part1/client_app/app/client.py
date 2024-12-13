from flask import Flask, request
import requests

app = Flask(__name__)

@app.route("/client")
def home():
    number = request.args.get('number')
    
    try:
        response = requests.get(
            f'http://server:3000/server?number={number}'
        )
        return response.text
    except Exception as e:
        return 'Invalid request'
    

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8000)
