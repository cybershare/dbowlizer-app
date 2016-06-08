<html>
<head>
<meta charset="UTF-8">
<title>DBOwlizer Endpoint Service</title>
</head>
<body>
<p>You have accessed the <a href="//dbowlizer.cybershare.utep.edu">DBOwlizer Project</a> demo endpoint service.</p>
<p>This demo webservice is deployed into Tomcat 8 and uses a sample database (employees) loaded on MySQL 5.6</p>
<p style="color:red"><b>Note: Running the database test case requires about 2 minutes to process, thank you for your patience.</b></p>	
    <form id="dbowl" name="dbowl" onsubmit="false">
		<textarea readonly="readonly" name="dbowlspec" id="dbowlspec" cols="100" rows="25">
{
  "dbsettings": {
    "driver": "mysql",
    "host": "ilinkbeta.cybershare.utep.edu",
    "port": "3306",
    "dbname": "employees",
    "user": "employees",
    "password": "emp@@ilink"
  }
}
		</textarea>
		<br>
		<input type="button" name="button" id="button" value="Run DBOwlizer" onclick="resultLinks();"/>
    <div id="result">
    </div>
    <script type="text/javascript" src="dbowlizerCall.js"></script>
    <script type="text/javascript" src="jquery-2.2.4.min.js"></script>
	</form>
	
	<div id="results">
	
	</div>
	
</body>
</html>
