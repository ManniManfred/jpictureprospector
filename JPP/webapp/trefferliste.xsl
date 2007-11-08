<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/">
<html>
<head>
<title>JPictureProspector</title>
</head>
<body>
<div>
<xsl:apply-templates />
</div>
</body>
</html>
</xsl:template>

<xsl:template match="BildDokument">
<div style="float: left; margin: 10px; padding: 10px; background-color: #CCEEEE">
	
	<xsl:element name="img">
		<xsl:attribute name="src">data:image/jpg;base64,<xsl:value-of select="Thumbnail"/></xsl:attribute>
	</xsl:element>
	
	<p align="center">
	  <xsl:element name="a">
	  
		<xsl:attribute name="href">http://www.google.de</xsl:attribute>
<!-- 		  <xsl:attribute name="href">file://<xsl:value-of select="Dateipfad"/></xsl:attribute> -->
			<xsl:value-of select="Dateiname"/>
    </xsl:element>
	</p>
</div>

</xsl:template>

</xsl:stylesheet>
