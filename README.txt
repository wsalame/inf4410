Readme

Compiler le projet gr�ce � la commande suivante :
�	ant

D�marrez le premier serveur. Le serveur va automatiquement charger le fichier �serverConfig�
�	./serveur

Dans le fichier �serverConfig�, vous pouvez modifier le port RMI, et le port d�acc�s, en plus du nombre d�op�rations allou�s (qui va permettre de calculer le taux de refus). Les serveurs n�ont que le mode non-s�curis�.
C�est donc dire que vous devez d�marrer un serveur � la fois, modifier le fichier, et ensuite partir le suivant.

Dans le fichier �serversHostname�, indiquez l�adresse des machines pour que le client puisse s�y connecter, ainsi que leur port du rmiregistry correspondant, s�par� par un espace. S�parez les diff�rentes machines en faisant un retour de ligne, c�est donc dire que chaque ligne correspond � une machine.
D�marrez le client, en lui passant aussi un argument, qui sera le chemin (path) du fichier contenant les op�rations

�	./client donnees-2606.txt
Voil�, ne reste plus qu�� attendre


