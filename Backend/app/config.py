class Config:

    SECRET_KEY = "12345"

    SQLALCHEMY_DATABASE_URI = "mysql+pymysql://root:@host.docker.internal/flask_auth_api"

    SQLALCHEMY_TRACK_MODIFICATIONS = False

    JWT_SECRET_KEY = "67890"