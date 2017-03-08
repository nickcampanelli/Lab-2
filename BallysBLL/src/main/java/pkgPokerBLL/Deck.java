package pkgPokerBLL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

import pkgPokerEnum.eRank;
import pkgPokerEnum.eSuit;

public class Deck {

	private UUID DeckID;
	private ArrayList<Card> DeckCards = new ArrayList<Card>();
	
	public Deck()
	{
		for (eRank Rank : eRank.values()) {
			for (eSuit Suit : eSuit.values()) {
				Card card = new Card(Rank,Suit);
				DeckCards.add(card);
			}
			System.out.println(Rank.getiRankNbr());
		}
		Collections.shuffle(DeckCards);
	}
	
	public Card DrawCard()
	{
		return DeckCards.remove(0);
	}
}
