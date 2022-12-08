dim objShell
	Set objShell = WScript.CreateObject( "WScript.Shell" )
	objShell.Run("cmd /c java -Xss1G -Xms2G -Xmx4G -jar EficienciaRelativa.jar"), 0, False