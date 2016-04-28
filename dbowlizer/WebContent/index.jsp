<html>
<head>
<meta charset="UTF-8">
<title>DBOwlizer Endpoint Service</title>
</head>
<body>
<p>You have accessed the <a href="">DBOwlizer Project</a> demo endpoint service.</p>
<p>Please submit your DBOwlizer specification in the text box below. An example specification can be found below.</p>
	<form id="dbowl" name="dbowl" method="post" action="./rest/">
		<textarea name="dbowlspec" id="dbowlspec" cols="100" rows="25">
{
  "dbsettings": {
    "driver": "mysql",
    "host": "ilinkbeta.cybershare.utep.edu",
    "port": "3306",
    "dbname": "dbowlizer",
    "user": "ilinkbetaadmin",
    "password": "ilinkb3t@@dm1n"
  }
}
		</textarea>
		<br>
		<input type="submit" name="button" id="button" value="Run DBOwlizer" />
	</form>
</body>
</html>
