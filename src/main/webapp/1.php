<meta charset="UTF-8">
<html>
<body>
hi
<br>
<br>
<?php
$chemin = $_POST['chemin'];
$fich = $_FILES['file']['tmp_name'];
echo "fichier avec parcourir : $fich <br/>";
echo "chemin écrit : $chemin <br/>";


$nomOrigine = $_FILES['file']['name'];
$elementsChemin = pathinfo($nomOrigine);
$extensionFichier = $elementsChemin['extension'];
$repertoireDestination = dirname(__FILE__)."/";
echo "1 : $nomOrigine <br/>";
echo "2 : $elementsChemin <br/>";
echo "3 : $extensionFichier <br/>";
echo "4 : $repertoireDestination <br/>";
echo "<br>";
$col = $_POST['colonne'];
echo "num de col : $col <br/>";
$algo1 = $_POST['algo1'];
$algo2 = $_POST['algo2'];
$algo3 = $_POST['algo3'];
echo "algo : $algo1, $algo2, $algo3";

?>
</body>
</html>