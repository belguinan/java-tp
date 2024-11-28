<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Contact Management TP1</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Lato:wght@400;700&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="css/style.css">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
    </head>
    <body class="bg-light d-flex flex-column h-100 align-items-center">
        <div class="d-flex flex-column rounded-3 my-auto w-100 mx-2" style="max-width: 600px;">

            <c:if test="${requestScope.message != null}">
                <div class="alert alert-success mb-4">
                    ${requestScope.message}
                </div>
            </c:if>

            <table class="bg-white table table-sm m-0 table-stipped table-borderless rounded-3 shadow-sm" style="overflow: hidden;">
                <thead class="table-dark">
                    <tr>
                        <th>
                            Name
                        </th>
                        <th>
                            Email
                        </th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${requestScope.contacts}" var="contact">
                        <tr>
                            <td>
                                ${contact.name()}
                            </td>
                            <td>
                                ${contact.email()}
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <c:if test="${requestScope.error != null}">
                <div class="alert alert-danger mt-4">
                    ${requestScope.error}
                </div>
            </c:if>

        </div>
    </body>
</html>