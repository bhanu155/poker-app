package com.sap.ase.poker.winner;

import static java.util.Collections.reverseOrder;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sap.ase.poker.model.Card;
import com.sap.ase.poker.model.Player;

/**
 * Finds the players that win the game, i.e. have the best hand on the table.
 * Can also be multiple players (=> split pot)
 */
public class FindWinners {

	public Winners apply(List<Player> players, List<Card> communityCards) {
		return new Winners(findWinners(mapPlayersWithBestHand(players, communityCards)), players.get(0));
	}

	private Map<Hand, Player> mapPlayersWithBestHand(List<Player> players, List<Card> communityCards) {
		Map<Hand, Player> map = new HashMap<>();
		for (Player player : players) {
			List<Card> sevenCards = new ArrayList<>(player.getCards());
			sevenCards.addAll(communityCards);
			Hand hand = new FindBestHand().apply(sevenCards);
			map.put(hand, player);
		}
		return map;
	}

	private List<Player> findWinners(Map<Hand, Player> map) {
		List<Hand> hands = map.keySet().stream().sorted(reverseOrder()).collect(toList());
		List<Player> winners = new ArrayList<>();
		for (int i = 0; i < hands.size(); i++) {
			if ((i == 0) || (hands.get(i).compareTo(hands.get(i - 1)) == 0)) {
				Player winner = map.get(hands.get(i));
				winners.add(winner);
			}
		}

		return winners;
	}
	
	public static class Winners {
		public final List<Player> list;
		public Player oddChipsWinner;
		public Winners(List<Player> list, Player oddChipsWinner) {
			this.list = list;
			this.oddChipsWinner = oddChipsWinner;
		}
	}
}
