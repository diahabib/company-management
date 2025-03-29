<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Connexion</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        .login-container {
            max-width: 500px;
            margin: 5rem auto;
            padding: 2.5rem;
            border-radius: 10px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            background: white;
        }

        .login-header {
            text-align: center;
            margin-bottom: 2rem;
        }

        .login-header h1 {
            color: #4361ee;
            margin-bottom: 0.5rem;
        }

        .login-header p {
            color: #6c757d;
        }

        .form-group {
            margin-bottom: 1.5rem;
        }

        .form-group label {
            display: block;
            margin-bottom: 0.5rem;
            font-weight: 500;
            color: #495057;
        }

        .form-control {
            width: 100%;
            padding: 0.75rem 1rem;
            border: 1px solid #ced4da;
            border-radius: 5px;
            font-size: 1rem;
            transition: border-color 0.3s;
        }

        .form-control:focus {
            border-color: #4361ee;
            outline: none;
            box-shadow: 0 0 0 3px rgba(67, 97, 238, 0.1);
        }

        .btn-login {
            width: 100%;
            padding: 0.75rem;
            background-color: #4361ee;
            color: white;
            border: none;
            border-radius: 5px;
            font-size: 1rem;
            font-weight: 500;
            cursor: pointer;
            transition: background-color 0.3s;
        }

        .btn-login:hover {
            background-color: #3a56d4;
        }

        .login-footer {
            text-align: center;
            margin-top: 1.5rem;
            color: #6c757d;
        }

        .input-icon {
            position: relative;
        }

        .input-icon i {
            position: absolute;
            top: 50%;
            left: 1rem;
            transform: translateY(-50%);
            color: #6c757d;
        }

        .input-icon input {
            padding-left: 3rem;
        }
    </style>
</head>
<body>
<div class="login-container">
    <div class="login-header">
        <h1><i class="fas fa-lock"></i> Connexion</h1>
        <p>Accédez à votre espace de gestion</p>
    </div>

    <c:if test="${not empty param.logout}">
        <div class="alert alert-success">
            Vous avez été déconnecté avec succès.
        </div>
    </c:if>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger">
                ${errorMessage}
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/login" method="post">
        <div class="form-group input-icon">
            <i class="fas fa-user"></i>
            <input type="text" id="username" name="username" class="form-control"
                   placeholder="Nom d'utilisateur" required>
        </div>

        <div class="form-group input-icon">
            <i class="fas fa-key"></i>
            <input type="password" id="password" name="password" class="form-control"
                   placeholder="Mot de passe" required>
        </div>

        <button type="submit" class="btn btn-login">
            <i class="fas fa-sign-in-alt"></i> Se connecter
        </button>

        <div class="login-footer">
            <p>Vous n'avez pas de compte? <a href="#">Contactez l'administrateur</a></p>
        </div>
    </form>
</div>
</body>
</html>