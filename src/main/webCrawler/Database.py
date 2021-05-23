import mysql.connector

mydb = mysql.connector.connect(
    host="localhost",
    user="root",
    passwd="",
    database="resources"
)
print(mydb)
mycursor = mydb.cursor()


def create_db():
    mycursor.execute("CREATE DATABASE resources")


def show_db():
    mycursor.execute("SHOW DATABASES")


def show_table():
    mycursor.execute("SHOW TABLES")
    for x in mycursor:
        print(x)


def create_table():
    mycursor.execute(
        "CREATE TABLE resource ( id INT unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY, link VARCHAR(10000) NOT NULL, title BLOB, body BLOB)")


def insert_table(tableName, link, title, body):
    sql = "INSERT INTO " + tableName + " (link, title,body) VALUES (%s, %s,%s)"
    val = [
        (link, title, body),
    ]
    mycursor.executemany(sql, val)


def delete(commands):
    mycursor.execute(" DELETE FROM resource WHERE " + commands)


def select_all():
    mycursor.execute("SELECT * FROM resources")
    for x in mycursor:
        print(x)


def alter_table():
    # behtare k table ro az java ezafe koni na inja
    mycursor.execute("ALTER TABLE table_name ADD COLUMN column name VARCHAR(15) AFTER name;")
