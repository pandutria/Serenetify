import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.preprocessing import LabelEncoder

def preprocess_data(file_path):
    df = pd.read_csv(file_path)
    df.columns = df.columns.str.strip()
    df['Text'] = df['Text'].str.lower()
    vectorizer = TfidfVectorizer(stop_words='english')
    X = vectorizer.fit_transform(df['Text'])
    label_encoder = LabelEncoder()
    y = label_encoder.fit_transform(df['Emotion'])
    
    return X, y, vectorizer, label_encoder
