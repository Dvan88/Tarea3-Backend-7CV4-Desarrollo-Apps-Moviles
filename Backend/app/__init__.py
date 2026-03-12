from flask import Flask
from flask_sqlalchemy import SQLAlchemy
from flask_bcrypt import Bcrypt
from flask_jwt_extended import JWTManager
from flask_cors import CORS
from flasgger import Swagger
import logging

from .config import Config

db = SQLAlchemy()
bcrypt = Bcrypt()
jwt = JWTManager()


def create_app():

    app = Flask(__name__)
    app.config.from_object(Config)

    db.init_app(app)
    bcrypt.init_app(app)
    jwt.init_app(app)

    CORS(app)

    Swagger(app)

    logging.basicConfig(
        level=logging.INFO,
        format="%(asctime)s %(levelname)s %(message)s"
    )

    from .routes.auth_routes import auth_bp
    from .routes.user_routes import user_bp

    app.register_blueprint(auth_bp)
    app.register_blueprint(user_bp)

    with app.app_context():
        db.create_all()

    return app