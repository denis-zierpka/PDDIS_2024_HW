from flask import Flask, request

app = Flask(__name__)

@app.route("/server")
def home():
    number = request.args.get('number')
    
    try:
        number = int(number)
        result = number * number
        return str(result)
    except Exception:
        return 'Invalid request'

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=3000)
