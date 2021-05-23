from flask import Flask, render_template
import pandas as pd
from sklearn.metrics import accuracy_score
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.model_selection import train_test_split
import pickle
from sklearn.svm import LinearSVC

app = Flask(__name__)


@app.route('/')
def index():
    return render_template('index.html')


@app.route('/get/<sentence>', methods=['GET'])
def get(sentence):
    value = training(sentence)
    print(sentence)
    return render_template('secondPage.html', sentence=sentence, value=value[0])

def training(sentence):
    data = pd.read_csv('data.csv')

    lsvc = LinearSVC()
    x_train, x_test, y_train, y_test, indices_train, indices_test = train_test_split(data.ErroneousSentence,
                                                                                     data.CorrectWord, data.index,
                                                                                     test_size=0.2, random_state=0)
    vectorizer = CountVectorizer()
    x_train_vect = vectorizer.fit_transform(x_train)
    x_test_vect = vectorizer.transform(x_test)
    model = lsvc.fit(x_train_vect, y_train)
    with open("model.pkl", 'wb') as file:
        pickle.dump(model, file)

    prediction = model.predict(x_test_vect)
    score = model.score(x_train_vect, y_train)
    print("Score : ", score)
    print("Accuracy: {:.2f}%".format(accuracy_score(y_test, prediction) * 100))
    print("Training is finished!")

    data_series = pd.Series(sentence)
    test_vect = vectorizer.transform(data_series)
    predictedValue = model.predict(test_vect)
    print(predictedValue)
    return predictedValue


if __name__ == '__main__':
    app.run()
