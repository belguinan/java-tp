<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Contact Management TP1</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Lato:wght@400;700&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="css/style.css">
    </head>
    <body class="bg-light d-flex flex-column h-100 align-items-center">
        <form
            action="${pageContext.request.contextPath}/contacts" 
            method="POST" 
            class="bg-white d-flex flex-column p-4 border border-light rounded-6 shadow-sm my-auto w-100 mx-2"
            style="max-width: 600px;"
        >
            <h1 class="h3 mb-3">New Contact</h1>

            <div class="mb-3">
                <label class="form-label">Full name</label>
                <input value="${param.name}" name="name" type="text" class="form-control">
            </div>
            
            <div class="mb-4">
                <label class="form-label">Email address</label>
                <input value="${param.email}" name="email" type="email" class="form-control">
            </div>

            <div class="btn-group ms-auto">
                <a href="${pageContext.request.contextPath}" class="btn btn-light">Reset</a>
                <button type="submit" class="btn btn-primary">Submit</button>
            </div>

            <c:if test="${requestScope.error != null}">
                <div class="alert alert-danger small mt-3 mb-0">
                    ${requestScope.error}
                </div>
            </c:if>

            <c:if test="${requestScope.contacts.size() > 0}">
                <div class="px-3 pt-3 pb-4 rounded-6 mt-5 bg-light">
                    <h2 class="h5 small mb-2">Available contacts:</h2>
                    <ul class="list-group border-0">
                        <c:forEach items="${requestScope.contacts.all()}" var="contact">

                            <li class="list-group-item d-flex justify-content-between align-items-start">
                                <div class="ms-2 me-auto">
                                    <div>
                                        <span class="text-muted">Name:</span> ${contact.getName()}
                                    </div>
                                    <span class="text-muted">Email:</span> ${contact.getEmail()}
                                </div>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </c:if>
            
        </form>
        
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
        <script src="js/main.js"></script>
    </body>
</html>