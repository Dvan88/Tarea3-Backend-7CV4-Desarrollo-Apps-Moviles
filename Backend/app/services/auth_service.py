from app.models.user import User
from app import db, bcrypt
from flask_jwt_extended import create_access_token


def register_user(username, password):

    if User.query.filter_by(username=username).first():
        return {"message": "User already exists"}, 400

    hashed_password = bcrypt.generate_password_hash(password).decode("utf-8")

    user = User(username=username, password=hashed_password)

    db.session.add(user)
    db.session.commit()

    return {"message": "User registered successfully"}, 201


def login_user(username, password):

    user = User.query.filter_by(username=username).first()

    if not user:
        return {"message": "User not found"}, 404

    if not bcrypt.check_password_hash(user.password, password):
        return {"message": "Invalid credentials"}, 401

    token = create_access_token(identity=username)

    return {
        "message": "Login successful",
        "username": username,
        "token": token
    }, 200