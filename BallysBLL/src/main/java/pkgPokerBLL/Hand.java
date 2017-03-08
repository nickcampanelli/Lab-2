package pkgPokerBLL;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;
import pkgPokerEnum.eCardNo;
import pkgPokerEnum.eHandStrength;
import pkgPokerEnum.eRank;



public class Hand {

	private UUID HandID;
	private boolean bIsScored;
	private HandScore HS;
	private ArrayList<Card> CardsInHand = new ArrayList<Card>();
	
	public Hand()
	{
		
	}
	
	public void AddCardToHand(Card c)
	{
		CardsInHand.add(c);
	}

	public ArrayList<Card> getCardsInHand() {
		return CardsInHand;
	}
	
	public HandScore getHandScore()
	{
		return HS;
	}
	
	public Hand EvaluateHand()
	{
		Hand h = Hand.EvaluateHand(this);
		return h;
	}
	
	private static Hand EvaluateHand(Hand h)  {

		Collections.sort(h.getCardsInHand());
		
		//	Another way to sort
		//	Collections.sort(h.getCardsInHand(), Card.CardRank);
		
		HandScore hs = new HandScore();
		try {
			Class<?> c = Class.forName("pkgPokerBLL.Hand");

			for (eHandStrength hstr : eHandStrength.values()) {
				Class[] cArg = new Class[2];
				cArg[0] = pkgPokerBLL.Hand.class;
				cArg[1] = pkgPokerBLL.HandScore.class;

				Method meth = c.getMethod(hstr.getEvalMethod(), cArg);
				Object o = meth.invoke(null, new Object[] { h, hs });

				// If o = true, that means the hand evaluated- skip the rest of
				// the evaluations
				if ((Boolean) o) {
					break;
				}
			}

			h.bIsScored = true;
			h.HS = hs;

		} catch (ClassNotFoundException x) {
			x.printStackTrace();
		} catch (IllegalAccessException x) {
			x.printStackTrace();
		} catch (NoSuchMethodException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return h;
	}
	
	public static boolean isFlush(ArrayList<Card> c) {
		boolean isFlush = false;
		int iCardCount = 0;
		
		for (int i = 0; i < 3; i++) {
			if (c.get(i).geteSuit().getiSuitNbr() != c.get(i+1).geteSuit().getiSuitNbr()) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean isStraight(ArrayList<Card> c) {
		boolean isStraight = true;
		int i = 0;
		if (Hand.isAce(c) == true) {
			i=1;
		} else {
			i=0;
		}
		for (; i < 3; i++) {
			if (c.get(i).geteRank().getiRankNbr() == c.get(i+1).geteRank().getiRankNbr() - 1)
			{
				isStraight = true;
			}
			else
			{
				isStraight = false;
				break;
			}
		}
		return isStraight;
	}
	
	public static boolean isAce(ArrayList<Card> c) {
		if (c.get(0).geteRank() == eRank.ACE)
			return true;
		else
			return false;
	}

	public static boolean isHandRoyalFlush(Hand h, HandScore hs)
	{
		boolean isRoyalFlush = false;
		ArrayList<Card> kickers = new ArrayList<Card>();
		if (isFlush(h.getCardsInHand()) && isStraight(h.getCardsInHand()) && isAce(h.getCardsInHand())) {
			isRoyalFlush = true;
			hs.setHandStrength(eHandStrength.RoyalFlush);
			hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank());
			hs.setLoHand(h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank());
			hs.setKickers(kickers);
		}
		return isRoyalFlush;
	}
	
	public static boolean isHandStraightFlush(Hand h, HandScore hs)
	{
		boolean isStraightFlush = false;
		ArrayList<Card> kickers = new ArrayList<Card>();
		if (isFlush(h.getCardsInHand()) && isStraight(h.getCardsInHand())) {
			isStraightFlush = true;
			hs.setHandStrength(eHandStrength.StraightFlush);
			hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank());
			hs.setLoHand(h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank());
			hs.setKickers(kickers);
		}
		return isStraightFlush;
	}
	
	public static boolean isHandFourOfAKind(Hand h, HandScore hs)
	{
		boolean isHandFourOfAKind = false;
		
		ArrayList<Card> kickers = new ArrayList<Card>();
		if ((h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank() == h.getCardsInHand()
				.get(eCardNo.FourthCard.getCardNo()).geteRank())){
			isHandFourOfAKind = true;
			hs.setHandStrength(eHandStrength.FourOfAKind);
			hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank());
			hs.setLoHand(h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()).geteRank());
		} else if ((h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank() == h.getCardsInHand()
				.get(eCardNo.FifthCard.getCardNo()).geteRank())){
			isHandFourOfAKind = true;
			hs.setHandStrength(eHandStrength.FourOfAKind);
			hs.setHiHand(h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank());
			hs.setLoHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank());
		}

		return isHandFourOfAKind;
	}	
	
	public static boolean isHandFlush(Hand h, HandScore hs)
	{
		boolean isFlush = false;
		ArrayList<Card> kickers = new ArrayList<Card>();
		if (isFlush(h.getCardsInHand())) {
			isFlush = true;
			hs.setHandStrength(eHandStrength.Flush);
			hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank());
			hs.setLoHand(h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank());
			hs.setKickers(kickers);
		}
		return isFlush;
	}
	
	public static boolean isHandStraight(Hand h, HandScore hs)
	{
		boolean isStraight = false;
		ArrayList<Card> kickers = new ArrayList<Card>();
		if (isStraight(h.getCardsInHand())) {
			isStraight = true;
			hs.setHandStrength(eHandStrength.Straight);
			hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank());
			hs.setLoHand(h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank());
			hs.setKickers(kickers);
		}
		return isStraight;
	}
	
	public static boolean isHandThreeOfAKind(Hand h, HandScore hs)
	{
		
		boolean isHandThreeOfAKind = false;
		
		ArrayList<Card> kickers = new ArrayList<Card>();
		if((h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank() == h.getCardsInHand()
				.get(eCardNo.ThirdCard.getCardNo()).geteRank())
				&& (h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank() != h.getCardsInHand()
				.get(eCardNo.FifthCard.getCardNo()).geteRank()))
		{
			hs.setHandStrength(eHandStrength.ThreeOfAKind);
			isHandThreeOfAKind = true;
		}
		else if((h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank() == h.getCardsInHand()
				.get(eCardNo.FourthCard.getCardNo()).geteRank()))
		{
			hs.setHandStrength(eHandStrength.ThreeOfAKind);
			isHandThreeOfAKind = true;
		}
		else if((h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteRank() == h.getCardsInHand()
				.get(eCardNo.FifthCard.getCardNo()).geteRank())
				&& (h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank() != h.getCardsInHand()
				.get(eCardNo.SecondCard.getCardNo()).geteRank()))
		{
			hs.setHandStrength(eHandStrength.ThreeOfAKind);
			isHandThreeOfAKind = true;
		}
		return isHandThreeOfAKind;
	}		
	
	public static boolean isHandTwoPair(Hand h, HandScore hs)
	{
		boolean isHandTwoPair = false;
		
		ArrayList<Card> kickers = new ArrayList<Card>();
		if((h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank() == h.getCardsInHand()
				.get(eCardNo.SecondCard.getCardNo()).geteRank())
				&& (h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteRank() == h.getCardsInHand()
				.get(eCardNo.FourthCard.getCardNo()).geteRank()))
		{
			isHandTwoPair = true;
		}
		else if((h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank() == h.getCardsInHand()
				.get(eCardNo.ThirdCard.getCardNo()).geteRank())
				&& (h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()).geteRank() == h.getCardsInHand()
				.get(eCardNo.FourthCard.getCardNo()).geteRank()))
		{
			isHandTwoPair = true;
		}
		else if((h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank() == h.getCardsInHand()
				.get(eCardNo.SecondCard.getCardNo()).geteRank())
				&& (h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()).geteRank() == h.getCardsInHand()
				.get(eCardNo.FourthCard.getCardNo()).geteRank()))
		{
			isHandTwoPair = true;
		}
		return isHandTwoPair;
	}	
	
	public static boolean isHandPair(Hand h, HandScore hs)
	{
		boolean isHandPair = false;
		
		ArrayList<Card> kickers = new ArrayList<Card>();
		
			if((h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank() == h.getCardsInHand()
					.get(eCardNo.SecondCard.getCardNo()).geteRank()))
					{
					isHandPair = true;
					}
			else if((h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank() == h.getCardsInHand()
					.get(eCardNo.ThirdCard.getCardNo()).geteRank()))
			{
				isHandPair = true;
			}
			else if((h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteRank() == h.getCardsInHand()
					.get(eCardNo.FourthCard.getCardNo()).geteRank()))
			{
				isHandPair = true;
			}
			else if((h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank() == h.getCardsInHand()
					.get(eCardNo.FifthCard.getCardNo()).geteRank()))
			{
				isHandPair = true;
			}
		return isHandPair;
	}	
	
	public static boolean isHandHighCard(Hand h, HandScore hs)
	{
		boolean isHighCard = false;
		
		ArrayList<Card> kickers = new ArrayList<Card>();
		if ((h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank() != h.getCardsInHand()
				.get(eCardNo.SecondCard.getCardNo()).geteRank())
				&& (h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank() != h.getCardsInHand()
				.get(eCardNo.ThirdCard.getCardNo()).geteRank()) && (h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteRank() != h.getCardsInHand()
				.get(eCardNo.FourthCard.getCardNo()).geteRank()) && (h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank() != h.getCardsInHand()
				.get(eCardNo.FifthCard.getCardNo()).geteRank())) {
			isHighCard = true;
			hs.setHandStrength(eHandStrength.HighCard);
			hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank());
			hs.setLoHand(h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank());
		}
		return isHighCard;
	}	
	
	public static boolean isAcesAndEights(Hand h, HandScore hs)
	{
		boolean isAcesAndEights = false;
		
		ArrayList<Card> kickers = new ArrayList<Card>();
		
		if(isHandTwoPair(h, hs))
		{
			if(hs.getHiHand()== eRank.ACE && hs.getLoHand()== eRank.EIGHT && ((h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteRank()
					== h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank())))	
			{
				isAcesAndEights = true;
				hs.setHandStrength(eHandStrength.AcesAndEights);
				hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank());
				hs.setLoHand(h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteRank());
				hs.getKickers().add(h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()));
				
			}
			else if(hs.getHiHand()== eRank.ACE && hs.getLoHand()== eRank.EIGHT && ((h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank()
					== h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()).geteRank())))
			{
				isAcesAndEights = true;
				hs.setHandStrength(eHandStrength.AcesAndEights);
				hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank());
				hs.setLoHand(h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank());
				hs.getKickers().add(h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()));
			}
		}
		return isAcesAndEights;
	}	
	
	public static boolean isHandFullHouse(Hand h, HandScore hs) {

		boolean isFullHouse = false;
		
		ArrayList<Card> kickers = new ArrayList<Card>();
		if ((h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank() == h.getCardsInHand()
				.get(eCardNo.ThirdCard.getCardNo()).geteRank())
				&& (h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank() == h.getCardsInHand()
						.get(eCardNo.FifthCard.getCardNo()).geteRank())) {
			isFullHouse = true;
			hs.setHandStrength(eHandStrength.FullHouse);
			hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank());
			hs.setLoHand(h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank());
		} else if ((h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank() == h.getCardsInHand()
				.get(eCardNo.SecondCard.getCardNo()).geteRank())
				&& (h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteRank() == h.getCardsInHand()
						.get(eCardNo.FifthCard.getCardNo()).geteRank())) {
			isFullHouse = true;
			hs.setHandStrength(eHandStrength.FullHouse);
			hs.setHiHand(h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteRank());
			hs.setLoHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank());
		}

		return isFullHouse;

	}
}
