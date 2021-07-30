<?xml version="1.0" encoding="ISO-8859-1"?>

<!-- Stylesheet to convert JavaHelp index and TOC pages into HTML -->

<!-- This file is part of JMRI.  Copyright 2007, 2015.                            -->
<!--                                                                        -->
<!-- JMRI is free software; you can redistribute it and/or modify it under  -->
<!-- the terms of version 2 of the GNU General Public License as published  -->
<!-- by the Free Software Foundation. See the "COPYING" file for a copy     -->
<!-- of this license.                                                       -->
<!--                                                                        -->
<!-- JMRI is distributed in the hope that it will be useful, but WITHOUT    -->
<!-- ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or  -->
<!-- FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License  -->
<!-- for more details.                                                      -->

<xsl:stylesheet	version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<!-- Need to instruct the XSLT processor to use HTML output rules.
     See http://www.w3.org/TR/xslt#output for more details
-->
<xsl:output method="html" encoding="UTF-8"/>

<!-- This first template matches our root element in the input file.
     This will trigger the generation of the header.
     In between we let the processor recursively process any contained
     elements, which is what the apply-templates instruction does.
     We also pick some stuff out explicitly in the head section using
     value-of instructions.
-->
<xsl:template match='index'>
    <xsl:text disable-output-escaping='yes'>&lt;!DOCTYPE html&gt;&#xA;</xsl:text>
    <html lang="en">
    <head>
    <title>JMRI Help System: Index</title>
    <meta name="author" content="Bob Jacobsen" />
    <meta name="keywords" content="JMRI Help Index" />

    <xsl:comment>=====================================================================</xsl:comment>
    <xsl:comment>This file is automatically generated by Ant from JmriHelp_enIndex.xml</xsl:comment>
    <xsl:comment>Do not edit it directly</xsl:comment>
    <xsl:comment>=====================================================================</xsl:comment>

    <xsl:comment>#include virtual="/Style.shtml" </xsl:comment>
    </head>

    <body>
    <xsl:comment>#include virtual="/Header.shtml" </xsl:comment>
    <xsl:comment>#include virtual="indexheader" </xsl:comment>
    <ul>
        <xsl:apply-templates/>
    </ul>

    <xsl:comment>#include virtual="/Footer.shtml" </xsl:comment>
    </body>
    </html>
</xsl:template>

<!-- recursively handle index items -->
<!-- if there's a target, convert to a HREF -->
<xsl:template match="indexitem">
    <li>
        <xsl:if test="@target" >
            <xsl:element name="a">
                <xsl:attribute name="href">
                    <xsl:variable name="target" select="@target" />
                    <xsl:for-each select="document('local/jmri_map.xml')/map/mapID">
                      <xsl:if test="( @target = $target )" >
                        <xsl:value-of select="@url"/>
                      </xsl:if>
                    </xsl:for-each>
                </xsl:attribute>
                <xsl:value-of select="@text"/>
            </xsl:element>
	    </xsl:if>
        <xsl:if test="not(@target)" >
                <xsl:value-of select="@text"/>
	    </xsl:if>

        <ul>
        <xsl:apply-templates/>
        </ul>
    </li>
</xsl:template>


<xsl:template match='toc'>
    <xsl:text disable-output-escaping='yes'>&lt;!DOCTYPE html&gt;&#xA;</xsl:text>
    <html lang="en">
    <head>
    <title>JMRI Help System: Table of Contents</title>
    <meta name="author" content="Bob Jacobsen" />
    <meta NAME="keywords" content="JMRI Help TOC Table of Contents" />

    <xsl:comment>=====================================================================</xsl:comment>
    <xsl:comment>This file is automatically generated by Ant from JmriHelp_enIndex.xml</xsl:comment>
    <xsl:comment>Do not edit it directly</xsl:comment>
    <xsl:comment>=====================================================================</xsl:comment>

    <xsl:comment>#include virtual="/Style.shtml" </xsl:comment>
    </head>

    <body>
    <xsl:comment>#include virtual="/Header.shtml" </xsl:comment>
    <xsl:comment>#include virtual="tocheader" </xsl:comment>
    <ul>
        <xsl:apply-templates/>
    </ul>

    <xsl:comment>#include virtual="/Footer.shtml" </xsl:comment>
    </body>
    </html>
</xsl:template>

<!-- recursively handle TOC items -->
<!-- if there's a target, convert to a HREF -->
<xsl:template match="tocitem">
    <li>
        <xsl:if test="@target" >
            <xsl:element name="a">
                <xsl:attribute name="href">
                    <xsl:variable name="target" select="@target" />
                    <xsl:for-each select="document('local/jmri_map.xml')/map/mapID">
                      <xsl:if test="( @target = $target )" >
                        <xsl:value-of select="@url"/>
                      </xsl:if>
                    </xsl:for-each>
                </xsl:attribute>
                <xsl:value-of select="@text"/>
            </xsl:element>
	    </xsl:if>
        <xsl:if test="not(@target)" >
                <xsl:value-of select="@text"/>
	    </xsl:if>

        <ul>
        <xsl:apply-templates/>
        </ul>
    </li>
</xsl:template>


</xsl:stylesheet>
