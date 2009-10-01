<?php
  /**
   * @version    $Id: mod_related_items.php 9030 2007-09-26 21:55:26Z jinx $
   * @package    Joomla
   * @copyright  Copyright (C) 2005 - 2007 Open Source Matters. All rights reserved.
   * @license    GNU/GPL, see LICENSE.php
   * Joomla! is free software. This version may have been modified pursuant
   * to the GNU General Public License, and as distributed it includes or
   * is derivative of works licensed under the GNU General Public License or
   * other free or open source software licenses.
   * See COPYRIGHT.php for copyright notices and details.
   */
   
  // no direct access
  defined('_JEXEC') or die('Restricted access');
   
  // Include the syndicate functions only once
  require_once (dirname(__FILE__).DS.'jpp/JPPClient.cls.php');
   
  try {
    $params = &$mainframe->getParams();
     
    $client = new JPPClient($params->get('jppserver'), 
      $params->get('user'), 
      $params->get('password'), 
      $params->get('gruppe'), 
      $params->get('sizeindex'));
     
    //echo $client->suche("jpg", 0, 20);
     
    $getparams = JRequest::get('get');
     
    if (isset($getparams["album"])) {
      if (isset($getparams["page"])) {
        showAlbum($client, $getparams["album"], $getparams["page"]);
      } else {
        showAlbum($client, $getparams["album"]);
      }
    } else {
      showOverview($client);
    }
  }
  catch (AuthenticationException $e) {
    echo "Fehler: " . $e->getMessage();
  }
   
   
  function showAlbum($client, $album, $page = 0) {
    LoadLightBox();

    global $mainframe;
    $params = &$mainframe->getParams();
    $menu = & JSite::getMenu();
    $item = $menu->getActive();
    
    $countPerPage = $params->get('countPerPage');
    $thumbWidth = $params->get('thumbWidth');
    $backLink = JRoute::_('index.php?option=com_jpp&Itemid=' . $item->id );
    
    $liste = $client->suche('Album:"' . $album . '"', $page * $countPerPage, $countPerPage);
    $count = $liste->getGesamtAnzahlTreffer();
    $l = $liste->getAnzahlTreffer();
 
    echo "<h2>$album</h2>";
    echo "<p>Anzahl Bilder: $count</p>\n\n";
    echo "\n\n<div style=\"clear:both;\"><p><a href=\"$backLink\">zurück</a></p></div>";


    // print page navigation
    echo '<p align="center"><font size="+1">&lt;';
    $maxPage = ceil(1.0 * $count / $countPerPage);
    for ($i = 0; $i < $maxPage; $i++) {
      $page_link = JRoute::_('index.php?option=com_jpp&album='. $album . '&Itemid=' . $item->id
        . '&page=' . $i);
      $link_output = '<a href="' . $page_link . '">' . ($i + 1) . '</a>';

      if ($page == $i) {
        echo '<b>' . ($i + 1) . '</b>';
      } else {
        echo $link_output;
      }

      if ($i < $maxPage - 1) {
        echo ', ';
      }

    }
    echo '&gt;</font></p>';


    echo "\n\n<div>";
    

    // print thumbs
    for ($i = 0; $i < $l; $i++) {
      $doc = $liste->getBildDokument($i);
      $thumbUrl = $doc->getMerkmal("JPP.MERKMALE.THUMBNAILMERKMAL");
      $imgUrl = $doc->getMerkmal("JPP.MERKMALE.DATEIPFADMERKMAL");

      echo '<div style="float: left; margin: 10px; padding: 10px; background-color: #CCEEEE">';
      echo '<a href="' . $imgUrl->getWert() . '" rel="lightbox[' . $album . ']">';
      echo '<img width="' . $thumbWidth .'" src="' . $thumbUrl->getWert() . '" border="0"/>';
      echo '</a></div>';
    }


    echo "</div>";
     
    echo "\n\n<div style=\"clear:both;\"><p><a href=\"$backLink\">zurück</a></p></div>";
  }
   
   
  function showOverview($client) {
     
    $menu = & JSite::getMenu();
    $item = $menu->getActive();
     
    // show all Alben
    echo "<h2>Alben</h2>\n";
    echo "<ul>\n";
    $alben = $client->getAlben();
    for ($i = 0; $i < count($alben); $i++) {
      $link = JRoute::_('index.php?option=com_jpp&album='. $alben[$i] . '&Itemid=' . $item->id );
      echo '<li><a href="' . $link . '">' . $alben[$i] . '</a></li>';
    }
    echo "\n</ul>";
     
     
  }
  //require(JModuleHelper::getLayoutPath('mod_photo'));
   






//****************    Lightbox       **********************/

function LoadLightBox() {
  // Load style Image Browser style sheet
  //HTML_imagebrowser::attachCSS();

  // Load the relevant scripts in each mode
  //HTML_imagebrowser::attachPopupScripts();
  attachLightBoxScripts();
}

function attachCSS() {
      ?>
<link rel="stylesheet" href="components/com_imagebrowser/styles.css" type="text/css" media="screen" />
      <?php
}

function attachLightBoxScripts() {
      ?>
      <script type="text/javascript" src="components/com_jpp/lib/lightbox2.03.3/js/prototype.js"></script>
      <script type="text/javascript" src="components/com_jpp/lib/lightbox2.03.3/js/scriptaculous.js?load=effects"></script>
      <script type="text/javascript" src="components/com_jpp/lib/lightbox2.03.3/js/lightbox.js"></script>

      <link rel="stylesheet" href="components/com_jpp/lib/lightbox2.03.3/css/lightbox.css" type="text/css" media="screen" />
      <?php
}




function attachPopupScripts() {
  ?>
  <script language="javascript" type="text/javascript">
  var popUpWin=0;
  function popUpWindow(URLStr, left, top, width, height) {
    if(popUpWin) {
      if(!popUpWin.closed) popUpWin.close();
    }
    popUpWinAttributes = 'toolbar=no,location=no,directories=no,status=no,menubar=no';
    popUpWinAttributes += ',scrollbars=no,resizable=no,copyhistory=yes,width='+width;
    popUpWinAttributes += ',height='+height+',left='+left+',top='+top+',screenX='+left+',screenY='+top;
    popUpWin = open(URLStr, 'popUpWin', popUpWinAttributes);
  }
  </script>
  <?php
}



?>

