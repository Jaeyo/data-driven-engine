<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="S" value="$" />
<!DOCTYPE html>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>data driven engine</title>

<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.0/jquery.min.js"></script>

<link rel="stylesheet" href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.9.2/themes/smoothness/jquery-ui.css">
<script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.9.2/jquery-ui.min.js"></script>

<script src="http://cdnjs.cloudflare.com/ajax/libs/jsPlumb/1.4.1/jquery.jsPlumb-1.4.1-all-min.js"></script>

<link rel="stylesheet" href="/resource/css/buttons.css" />
<script src="/resource/js/lib/buttons.js"></script>

<script src="/resource/js/lib/jquery.tmpl.min.js"></script>

<script src="/resource/js/lib/js_cols.min.js"></script>

<script src="/resource/js/lib/notify.min.js"></script>

<script src="/resource/js/common.js"></script>
<script src="/resource/js/component.js"></script>
<link rel="stylesheet" href="/resource/css/home2.css" />

</head>
<body>

<script src="/resource/js/home2.js"></script>
<script type="text/javascript">
var controller = new Controller();
</script>

<div class="container">
	<div class="menu-container">
		<div class="menu-container-top">
			<span class="button-dropdown" data-buttons="dropdown">
				<a href="#" class="button button-rounded menu-button">ADD COMPONENT</a>
				<ul class="button-dropdown-menu-below ">
					<li class="button-dropdown-divider"><a href="#" onclick="controller.addNewComponent('FileReader')">FileReader</a></li>
					<li class="button-dropdown-divider"><a href="#" onclick="controller.addNewComponent('RandomWordGenerator')">RandomWordGenerator</a></li>
					<li class="button-dropdown-divider"><a href="#" onclick="controller.addNewComponent('SimpleDeliver')">SimpleDeliver</a></li>
					<li class="button-dropdown-divider"><a href="#" onclick="controller.addNewComponent('ConsolePrinter')">ConsolePrinter</a></li>
				</ul>
			</span>
			<a href="#" class="button button-rounded menu-button">SETTING</a>
		</div>
		<div id="menuContainerBottom" class="menu-container-bottom">
		</div>
	</div>
	<div id="componentContainer" class="component-container">
	</div>
</div>

<script type="text/javascript">
controller.refreshMap();
</script>


<script id="componentTmpl" type="x-jquery-tmpl">
<div id="${S}{uuid}" class="component">
	<div class="component-running-signal"></div>
	<div class="component-type">${S}{type}</div>
	<div class="component-title">
		${S}{name}
		<a href="#" onclick="controller.viewRenameConfig('${S}{uuid}')"><img class="icon" src="/resource/icon/edit.png" /></a>
	</div>
	<div class="end-point"></div>
	<hr />
	<div class="component-oper-area">
		<a href="#" onclick="controller.viewConfig('${S}{uuid}')"><img class="icon" src="/resource/icon/config.png" /></a>
		<a href="#" onclick="controller.startComponent('${S}{uuid}')"><img class="icon" src="/resource/icon/start.png" /></a>
		<a href="#" onclick="controller.stopComponent('${S}{uuid}')"><img class="icon" src="/resource/icon/stop.png" /></a>
	</div>
</div>
</script>


<script id="configTmpl" type="x-jquery-tmpl">
<div class="config-menu">
	{{each configArr}}
		<label class="config-key">${S}{${S}value.key}</label>
		<input type="text" class="config-value" value="${S}{${S}value.value}" />
	{{/each}}
	{{if configArr.length != 0}}
		<br />
		<a href="#" class="button button-border menu-button" onclick="controller.setConfig('${S}{uuid}')">OK</a>
	{{/if}}
	<a href="#" id="btnRemoveComponent" onclick="controller.removeComponent('${S}{uuid}')"><img class="icon" src="/resource/icon/remove.png" /></a>
</div>
</script>


<script id="renameTmpl" type="x-jquery-tmpl">
<div class="rename-menu">
	<label class="config-key">name</label>
	<input type="text" class="config-value" value="${S}{name}" />
	<br />
	<a href="#" class="button button-border menu-button" onclick="controller.rename('${S}{uuid}')">OK</a>
</div>
</script>

</body>
</html>