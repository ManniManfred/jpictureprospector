<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/">

<div>
<xsl:apply-templates />
</div>

</xsl:template>

<xsl:template match="BildDokument">
<div style="float: left; margin: 10px; padding: 10px; background-color: #CCEEEE">
	
  <xsl:element name="img">
    <xsl:attribute name="src">data:image/jpg;base64,<xsl:value-of select="ThumbnailMerkmal"/></xsl:attribute>
  </xsl:element>
	
  <p align="center">
    <xsl:element name="a"><xsl:attribute name="href"><xsl:value-of select="DateipfadMerkmal"/></xsl:attribute>
      <xsl:value-of select="DateinameMerkmal"/>
    </xsl:element>
  </p>
</div>

</xsl:template>

</xsl:stylesheet>
