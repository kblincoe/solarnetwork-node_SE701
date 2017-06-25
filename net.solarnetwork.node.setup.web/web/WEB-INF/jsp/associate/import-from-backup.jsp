<p><fmt:message key="node.setup.restore.intro"/></p>

<setup:url value="/associate/importBackup" var="action"/>
<form:form action="${action}" method="post" cssClass="form-horizontal" enctype="multipart/form-data" id="associate-import-backup-form">
	<fieldset>
		<div class="control-group">
			<label class="control-label" for="restore-file">
				<fmt:message key="node.setup.restore.file"/>
			</label>
			<div class="controls">
				<input class="span3" type="file" name="file" id="restore-file"/>
			</div>
		</div>
	</fieldset>
	<div class="form-actions">
		<button type="submit" class="btn btn-primary ladda-button expand-right"><fmt:message key='node.setup.restore.upload'/></button>
	</div>
	<sec:csrfInput/>
</form:form>
