<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags"%>


<jsp:include page="../fragments/userheader.jsp" />
<div class="container">

	<nav class="navbar navbar-expand-lg navbar-light bg-light">
		<div class="container-fluid">



			<jsp:include page="../fragments/usermenu.jsp" />



		</div>
	</nav>






	<div>
		<h3>Prof home page</h3>
		<p>Hello and welcome to your application</p>

		<s:authorize access="isAuthenticated()">
    			You are connected with: 
    			 <s:authentication property="principal.username" /> <br>
			Your Email : <s:authentication property="principal.email" /><br>
			Your First Name : <s:authentication property="principal.firstName" /><br>
			Your Last name : <s:authentication property="principal.LastName" /><br>
		</s:authorize>
	</div>

	<div>

		<form
				action="${pageContext.request.contextPath}/fichier/searchStudents"
				class="d-flex" method="GET">
			<input name="titre" class="form-control me-2" type="search"
				   placeholder="Saisir le titre du module" aria-label="Search">
			<button  class="btn btn-outline-success" type="submit">Search</button>
		</form>
	</div>
	<div>

		<table class="table">
			<thead class="bg-light">
			<tr>
				<th scope="col">ID</th>
				<th scope="col">CNE</th>
				<th scope="col">Nom</th>
				<th scope="col">Prénom</th>

			</tr>
			</thead>

			<c:forEach items="${studentList}" var="s">
				<tr>

					<td><c:out value="${s.getIdUtilisateur()}" /></td>
					<td><c:out value="${s.getCne()}" /></td>
					<td><c:out value="${s.getNom()} / ${s.getNomArabe()}" /></td>
					<td><c:out value="${s.getPrenom()} / ${s.getPrenomArabe()}" /></td>


				</tr>

			</c:forEach>

		</table>

	</div>


		<form


				action="${pageContext.request.contextPath}/fichier/excel"
				class="" method="GET">
<div class="d-flex">
			<p class="me-3">
				Choisir Session :
			</p>
			<div>
			<div class="form-check">
				<input class="form-check-input" type="radio" name="session" id="session_normale" value="Normale">
				<label class="form-check-label" for="session_normale">
					Normale				</label>
			</div>
			<div class="form-check">
				<input class="form-check-input" type="radio" name="session" id="session_rattrapage" value="Rattrapage" >
				<label class="form-check-label" for="session_normale">
					Ratttrapage
				</label>
			</div>
			</div>

</div>
			<button  class="btn btn-outline-success text-end" type="submit">Export</button>

		</form>
	</div>



<jsp:include page="../fragments/userfooter.jsp" />

