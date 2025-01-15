from flask import Flask, request, jsonify
from preprocess.preprocess import preprocess_data
from model.mood_detector import train_model

app = Flask(__name__)
last_prediction = {}
last_text = ""

def predict_mood_probabilities(text, model, vectorizer, label_encoder):
    text = text.lower()
    vectorized_text = vectorizer.transform([text])
    probabilities = model.predict_proba(vectorized_text)[0]
    emotions = label_encoder.classes_
    result = {emotion: prob * 100 for emotion, prob in zip(emotions, probabilities)}
    return result

@app.route('/predict', methods=['POST'])
def predict():
    global last_prediction, last_text
    data = request.get_json()
    text = data['text']
    last_text = text
    X, y, vectorizer, label_encoder = preprocess_data('data/emotion_final.csv')
    model = train_model(X, y)
    last_prediction = predict_mood_probabilities(text, model, vectorizer, label_encoder)
    return jsonify({
        "text": {"user": text},
        "mood": last_prediction
    })

@app.route('/result', methods=['GET'])
def result():
    if last_prediction:
        return jsonify({
            "text": {"user": last_text},
            "mood": last_prediction
        })
    else:
        return jsonify({"error": "No prediction has been made yet."}), 400

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0' , port=4000)