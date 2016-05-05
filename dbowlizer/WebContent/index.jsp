<html>
<head>
<meta charset="UTF-8">
<title>DBOwlizer Endpoint Service</title>
</head>
<body>
<p>You have accessed the <a href="//dbowlizer.cybershare.utep.edu">DBOwlizer Project</a> demo endpoint service.</p>
<p>This demo webservice is deployed into Tomcat 8 and uses a sample database (employees) loaded on MySQL 5.6</p>
	<form id="dbowl" name="dbowl" method="post" action="./rest/">
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
		<input type="submit" name="button" id="button" value="Run DBOwlizer" />
	</form>
</body>
</html>
