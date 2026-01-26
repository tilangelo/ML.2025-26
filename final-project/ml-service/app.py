from fastapi import FastAPI
from pydantic import BaseModel
from preprocessing import preprocess
from model_loader import ModelBundle
import numpy as np
from scipy.sparse import hstack, csr_matrix

app = FastAPI()
models = ModelBundle()

class PredictRequest(BaseModel):
    text: str

class PredictResponse(BaseModel):
    cluster_id: int
    cluster_name: str

@app.post("/predictPy", response_model=PredictResponse)
def predict(request: PredictRequest):
    print(request)
    tokens = preprocess(request.text)
    text = " ".join(tokens)

    X = models.tfidf.transform([text])  # (1, 5000)
    predicted_cluster = int(models.classifier.predict(X)[0])

    cluster_names = {
        0: "Уголовные дела и суды",
        1: "Общая хроника края, области и тп",
        2: "Прогноз погоды и пр",
        3: "Общие городские новости",
        4: "Происшествия и ЧС (ДТП, пожары)"
    }

    return PredictResponse(
        cluster_id=predicted_cluster,
        cluster_name=cluster_names[predicted_cluster]
    )
