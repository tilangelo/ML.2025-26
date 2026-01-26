import joblib
from pathlib import Path

BASE_DIR = Path(__file__).resolve().parent
MODEL_DIR = BASE_DIR / "model"


class ModelBundle:
    def __init__(self):
        self.tfidf = joblib.load(MODEL_DIR / "tfidf.pkl")
        self.kmeans = joblib.load(MODEL_DIR / "kmeans.pkl")
        self.classifier = joblib.load(MODEL_DIR / "classifier.pkl")
