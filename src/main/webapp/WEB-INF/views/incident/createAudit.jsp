<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<form:form class="form" method="post" modelAttribute="auditForm">
	<h2 class="form-heading"><s:message code="incident.audit.add" /></h2>
	<form:errors path="" element="p" class="text-error" />
	
	<label for="status"><s:message code="incident.status" /></label>
	<form:select path="status" class="input-block-level">
		<c:forEach items="${auditForm.getAvailableStatuses()}" var="item">
			<s:message code="incident.status.${item}" var="label" />
			<form:option value="${item}" label="${label}"></form:option>
		</c:forEach>
	</form:select>
	<form:errors path="status" element="p" class="text-error"/> 
	
	<label for="status"><s:message code="incident.audit.description" /></label>
	<form:textarea path="description" class="input-block-level" rows="5" />
	<form:errors path="description" element="p" class="text-error"/> 
	
	<button class="btn btn-large btn-primary" type="submit"><s:message code="incident.audit.add" /></button>	
</form:form>