import os
from flask import Flask, request, jsonify
from flask_cors import CORS
from models import db, bcrypt, User

app = Flask(__name__)

# Configuración CORS para permitir conexiones desde Android
CORS(app, resources={r"/*": {"origins": "*"}})

# Configuración base de datos (desde variable de entorno o valor por defecto)
app.config['SQLALCHEMY_DATABASE_URI'] = os.getenv(
    "DATABASE_URL",
    "postgresql://postgres:postgres123@db:5432/user_auth_db"
)

app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False

db.init_app(app)
bcrypt.init_app(app)

# Crear tablas automáticamente
with app.app_context():
    db.create_all()
    print("✅ Base de datos lista")

# Validación simple de contraseña
def is_valid_password(password):
    return len(password) >= 6


@app.route('/', methods=['GET'])
def home():
    """Endpoint para verificar que la API está activa"""
    return jsonify({
        "status": "success",
        "service": "Flask Auth API",
        "version": "1.1.0"
    }), 200


@app.route('/register', methods=['POST'])
def register():
    """Registro de usuario"""
    try:
        data = request.get_json()

        if not data or 'username' not in data or 'password' not in data:
            return jsonify({
                "status": "error",
                "message": "Faltan username o password"
            }), 400

        username = data['username'].strip()
        password = data['password']

        if not username or not password:
            return jsonify({
                "status": "error",
                "message": "Los campos no pueden estar vacíos"
            }), 400

        if not is_valid_password(password):
            return jsonify({
                "status": "error",
                "message": "La contraseña debe tener al menos 6 caracteres"
            }), 400

        if User.query.filter_by(username=username).first():
            return jsonify({
                "status": "error",
                "message": "El usuario ya existe"
            }), 409

        user = User(username=username)
        user.set_password(password)

        db.session.add(user)
        db.session.commit()

        return jsonify({
            "status": "success",
            "message": "Usuario registrado correctamente",
            "user": user.to_dict()
        }), 201

    except Exception as e:
        db.session.rollback()
        print("❌ ERROR EN REGISTER:", e)

        return jsonify({
            "status": "error",
            "message": str(e)
        }), 500


@app.route('/login', methods=['POST'])
def login():
    """Login de usuario"""
    try:
        data = request.get_json()

        if not data or 'username' not in data or 'password' not in data:
            return jsonify({
                "status": "error",
                "message": "Faltan username o password"
            }), 400

        username = data['username'].strip()
        password = data['password']

        user = User.query.filter_by(username=username).first()

        if user and user.check_password(password):
            return jsonify({
                "status": "success",
                "message": "Login exitoso",
                "user": user.to_dict()
            }), 200
        else:
            return jsonify({
                "status": "error",
                "message": "Credenciales incorrectas"
            }), 401

    except Exception as e:
        print("Error en login:", e)

        return jsonify({
            "status": "error",
            "message": "Error en el servidor"
        }), 500


@app.route('/users', methods=['GET'])
def get_users():
    """Endpoint de depuración para listar usuarios"""
    try:
        users = User.query.all()

        return jsonify({
            "status": "success",
            "count": len(users),
            "users": [user.to_dict() for user in users]
        }), 200

    except Exception as e:
        print("Error en users:", e)

        return jsonify({
            "status": "error",
            "message": "Error al obtener usuarios"
        }), 500


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug=True)