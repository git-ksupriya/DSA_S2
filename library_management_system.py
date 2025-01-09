import mysql.connector as ctr

conn=ctr.connect(host='localhost', user='root', password='1232')
if conn.is_connected():
    print('hello, it\'s connected!')

print("hello world")
print("where did 2+3 go")
