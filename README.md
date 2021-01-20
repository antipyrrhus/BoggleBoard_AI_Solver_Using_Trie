This is a playable 1-player Boggle Game (For reference, see https://en.wikipedia.org/wiki/Boggle), along with an automated perfect solver to compare your performance against. The game can be played on a board size of 4x4 up to 10x10, and also has varying difficulty levels. 

The object of the game is to find all (or as many as possible) valid words of length > 2 on a given boggle board.

The automated solver uses the Trie data structure to efficiently search for and find all valid words on a given boggle board. (Note: The code contains two different Trie implementations -- 26-way Trie and Ternary Trie. The latter is slightly slower than the former but is more space-efficient. The 26-way Trie is used for the game, whereas the Ternary Trie code is merely provided as an academic reference.)

To play, execute BoggleGame with an optional parameter of [# of rows] [# of cols] (default is 4 x 4 board).
