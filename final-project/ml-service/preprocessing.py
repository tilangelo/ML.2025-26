import re
from typing import List


def clean_text(text: str) -> str:
    """
    Очистка текста:
    - нижний регистр
    - удаление лишних символов
    """
    text = text.lower()
    text = re.sub(r"[^а-яa-zё\s]", " ", text)
    text = re.sub(r"\s+", " ", text).strip()
    return text


def tokenize(text: str) -> List[str]:
    """
    Простейшая токенизация
    """
    return text.split()


def preprocess(text: str) -> List[str]:
    """
    Полный пайплайн предобработки
    """
    cleaned = clean_text(text)
    tokens = tokenize(cleaned)
    return tokens
