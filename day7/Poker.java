package day7;

import Utils.AdventUtils;

import java.util.*;

public class Poker {

    public enum HandType {
        HIGH_CARD,
        ONE_PAIR,
        TWO_PAIR,
        THREE_OF_A_KIND,
        FULL_HOUSE,
        FOUR_OF_A_KIND,
        FIVE_OF_A_KIND
    }
    private static final String TEST_INPUT = "day7/testInput.txt";
    private static final String INPUT = "day7/input.txt";

    private static final Map<Character, Integer> cardToIntegerMap = Map.ofEntries(
            Map.entry('T', 10),
            Map.entry('J', 1),
            Map.entry('Q', 12),
            Map.entry('K', 13),
            Map.entry('A', 14));
    List<HandAndBid> handAndBidList = new ArrayList<>();

    Poker(String filename) {
        AdventUtils.parseFileInput(filename, this::addHandAndBidToMap);
    }

    public void addHandAndBidToMap(String line) {
        String[] tokens = line.split(" ");
        handAndBidList.add(new HandAndBid(tokens[0], Integer.parseInt(tokens[1])));
    }

    public long findPointsTotal(boolean useJoker) {
        List<HandAndBid> sortedList = new ArrayList<>(handAndBidList);
        if (useJoker) {
            for (HandAndBid handAndBid : sortedList) {
                handAndBid.calculateHandTypeWithJoker();
            }
        }
        sortedList.sort(new HandAndBidComparator());
        System.out.println("Sorted list = \n" + sortedList);
        long result = 0;
        for (int i = 0; i < sortedList.size(); i++) {
            result += (long) (i + 1) * sortedList.get(i).getBid();
        }
        return result;
    }

    public static Map<Character, Integer> createCharacterOccurenceMap(String string) {
        Map<Character, Integer> characterOccurenceMap = new HashMap<>();
        char[] charArray =  string.toCharArray();
        Arrays.sort(charArray);
        for (char c : charArray) {
            if (!characterOccurenceMap.containsKey(c)) {
                characterOccurenceMap.put(c, 0);
            }
            characterOccurenceMap.put(c, characterOccurenceMap.get(c) + 1);
        }
        return characterOccurenceMap;
    }

    public static HandType calculateHandRankFromOccurrenceMap(Map<Character, Integer> occurenceMap, Boolean useJoker) {
        boolean jokerPresent = occurenceMap.containsKey('J') && useJoker;
        int mapSize = occurenceMap.size();
        HandType handType;
        if (mapSize <= 1) {
            handType = HandType.FIVE_OF_A_KIND;
        } else if (mapSize == 2) {
            handType = (occurenceMap.values().stream().anyMatch(i -> i == 4)) ? HandType.FOUR_OF_A_KIND : HandType.FULL_HOUSE;
        } else if (mapSize == 3) {
            handType = (occurenceMap.values().stream().anyMatch(i -> i == 3)) ? HandType.THREE_OF_A_KIND : HandType.TWO_PAIR;
        } else if (mapSize == 4) {
            handType = HandType.ONE_PAIR;
        } else {
            handType = HandType.HIGH_CARD;
        }

        if (jokerPresent) {
            handType = switch (handType) {
                case HIGH_CARD -> HandType.ONE_PAIR;
                case ONE_PAIR -> HandType.THREE_OF_A_KIND;
                case TWO_PAIR -> (occurenceMap.get('J') == 2) ? HandType.FOUR_OF_A_KIND : HandType.FULL_HOUSE;
                case THREE_OF_A_KIND -> HandType.FOUR_OF_A_KIND;
                case FULL_HOUSE, FOUR_OF_A_KIND -> HandType.FIVE_OF_A_KIND;
                default -> handType;
            };
        }
        return handType;
    }

    public static class HandAndBid {

        public String handString;
        private final List<Integer> hand;

        private final Map<Character, Integer> occurenceMap;
        private final Integer bid;

        private HandType handType;

        public HandAndBid(String hand, Integer bid) {
            assert hand.length() == 5: "Hand must contain 5 cards";
            this.handString = hand;
            this.hand = new ArrayList<>(hand.chars().mapToObj(i -> convertCardToInt((char)i)).toList());
            this.bid = bid;
            this.occurenceMap = createCharacterOccurenceMap(hand);
            calculateHandTypeWithoutJoker();
        }

        public void calculateHandTypeWithoutJoker() {
            this.handType = calculateHandRankFromOccurrenceMap(occurenceMap, false);
        }
        public void calculateHandTypeWithJoker() {
            for (int i = 0; i < hand.size(); i++) {
                if (hand.get(i).equals(cardToIntegerMap.get('J'))) {
                    hand.set(i,1);
                }
            }
            this.handType = calculateHandRankFromOccurrenceMap(occurenceMap, true);
        }

        public static int convertCardToInt(Character card) {
            if (card >= '2' && card <= '9') {
                return Character.digit(card, 10);
            } else {
                return cardToIntegerMap.get(card);
            }
        }

        public Integer getBid() {
            return bid;
        }

        public List<Integer> getHand() {
            return hand;
        }

        @Override
        public String toString() {
            return handString + ": " + handType.name() + "\n";
        }
    }

    static class HandAndBidComparator implements java.util.Comparator<HandAndBid> {
        @Override
        public int compare(HandAndBid a, HandAndBid b) {
            if (a.handType != b.handType) {
                return a.handType.ordinal() - b.handType.ordinal();
            }
            for (int i = 0; i < a.getHand().size(); i++) {
                if (!Objects.equals(a.getHand().get(i), b.getHand().get(i))){
                    return a.getHand().get(i) - b.getHand().get(i);
                }
            }
            return 0;
        }
    }

    public static void main(String[] args) {
        for (String filename : Arrays.asList(TEST_INPUT, INPUT)) {
            Poker poker = new Poker(filename);
            System.out.println("Result1(without joker) = " + poker.findPointsTotal(false));
            System.out.println("Result2(with joker) = " + poker.findPointsTotal(true));
        }
    }
}
