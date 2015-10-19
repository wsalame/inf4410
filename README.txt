Instructions pour linux
-----------------
Pour exécuter la partie 1:
1. Allez dans le dossier bin, et partez rmiregistry en tappant "rmiregistry" dans la console. Laissez la console ouverte.
2. (Optionnel) Le projet est déjà compilé, mais vous pouvez le recompiler en tappant "./ant" à la racine du projet
3. Partez le serveur en tappant "./server". Laissez la console ouverte
4. Partez le client en exécutant le fichier bash. Il faut aussi lui donner un paramètre indiquant lequel des serveurs on veut appeler
	3 choix :
		- local
		- localrmi
		- distantrmi
	Exemple : ./script.sh local


-----------------
Pour exécuter la partie 2:

1. Allez dans le dossier bin, et partez rmiregistry en tappant "rmiregistry" dans la console. Laissez la console ouverte.
2. (Optionnel) Le projet est déjà compilé, mais vous pouvez le recompiler en tappant "./ant" à la racine du projet
3. Partez le serveur en tappant "./server". Laissez la console ouverte
4. Partez le client en tappant "./client". Il faut aussi ajouter une commande en paramètre, et au besoin (selon la commande), un paramètre de plus
	Commandes :
		- create <nomDuFichier>
		- list
		- syncLocalDir
		- get <nomDuFichier>
		- lock <nomDuFichier>
		- push <nomDuFichier>

	Exemple : ./client list
	