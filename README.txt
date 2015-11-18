Readme

Compiler le projet grâce à la commande suivante :
•	ant

Démarrez le premier serveur. Le serveur va automatiquement charger le fichier “serverConfig”
•	./serveur

Dans le fichier “serverConfig”, vous pouvez modifier le port RMI, et le port d’accès, en plus du nombre d’opérations alloués (qui va permettre de calculer le taux de refus). Les serveurs n’ont que le mode non-sécurisé.
C’est donc dire que vous devez démarrer un serveur à la fois, modifier le fichier, et ensuite partir le suivant.

Dans le fichier “serversHostname”, indiquez l’adresse des machines pour que le client puisse s’y connecter, ainsi que leur port du rmiregistry correspondant, séparé par un espace. Séparez les différentes machines en faisant un retour de ligne, c’est donc dire que chaque ligne correspond à une machine.
Démarrez le client, en lui passant aussi un argument, qui sera le chemin (path) du fichier contenant les opérations

•	./client donnees-2606.txt
Voilà, ne reste plus qu’à attendre


