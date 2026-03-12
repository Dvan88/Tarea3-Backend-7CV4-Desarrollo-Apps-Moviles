from flask import Blueprint, request, jsonify
from app.services.auth_service import register_user, login_user

auth_bp = Blueprint("auth", __name__, url_prefix="/api/v1")


@auth_bp.route("/", methods=["GET"])
def home():
    """
    Estatus de la API
    ---
    tags:
      - Sistema
    responses:
      200:
        description: API running
    """
    return jsonify({"status": "API running"})


@auth_bp.route("/health", methods=["GET"])
def health():
    """
    Chequeo de Salud
    ---
    tags:
      - Sistema
    responses:
      200:
        description: Server Saludable
    """
    return jsonify({"status": "ok"})


@auth_bp.route("/register", methods=["POST"])
def register():
    """
    Registrar Usuario
    ---
    tags:
      - Autenticación
    parameters:
      - name: body
        in: body
        required: true
        schema:
          properties:
            Nombre:
              type: string
            Contraseña:
              type: string
    responses:
      201:
        description: Usuario Registrado
      400:
        description: Usuario Existente 
    """

    data = request.get_json()

    username = data.get("username")
    password = data.get("password")

    if not username or not password:
        return jsonify({"message": "Se requiere de de nombre y contraseña"}), 400

    return register_user(username, password)


@auth_bp.route("/login", methods=["POST"])
def login():
    """
    Login
    ---
    tags:
      - Autenticación
    parameters:
      - name: body
        in: body
        required: true
        schema:
          properties:
            Nombre:
              type: string
            Contraseña:
              type: string
    responses:
      200:
        description: Inicio de Sesión Exitoso
      401:
        description: Credenciales invalidas
    """

    data = request.get_json()

    username = data.get("username")
    password = data.get("password")

    return login_user(username, password)