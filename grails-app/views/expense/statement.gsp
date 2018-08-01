<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'expense.label', default: 'Expense')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
<a href="#list-expense" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
<div class="nav" role="navigation">
    <ul>
        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
        <li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
    </ul>
</div>
<div id="list-expense" class="content scaffold-list" role="main">
    <h1>Statement of Account for ${user.name}</h1><br>
    <h2>Initial Balance : ${user.initialBankBalance}</h2>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>

    <table>
        <tr>
            <th>Date of transaction</th>
            <th>Description</th>
            <th>Amount(Rand)</th>
            <th>Amount(USD)</th>
            <th>Running balance</th>
        </tr>
        <g:each var="expense" in="${user.expenses}">
         <tr>
             <td>${expense.dateCreated}</td>
             <td>${expense.description}</td>
             <td>${expense.amount}</td>
             <td>${expense.convertedAmount}</td>
             <td>${expense.runningBalance}</td>
         </tr>
        </g:each>
    </table>

    <div class="pagination">
        <g:paginate total="${expenseCount ?: 0}" />
    </div>
    <div>
        <g:link controller="expense" action="downloadCSV" id="${user.id}" >Download CSV</g:link>
    </div>
</div>
</body>
</html>