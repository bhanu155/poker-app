package com.sap.ase.poker.winner;

import static java.util.Collections.reverseOrder;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sap.ase.poker.model.Card;
import com.sap.ase.poker.model.Player;

public class FindWinners {

	public List<Player> findWinners(List<Player> players, List<Card> communityCards) {
		return findWinners(mapPlayersWithBestHand(players, communityCards));
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
				winners.add(map.get(hands.get(i)));
			}
		}

		return winners;
	}
}
