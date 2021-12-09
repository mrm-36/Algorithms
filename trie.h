#pragma once

#include <string>

class TrieNode {
private:
  TrieNode* *children;
  const int ALPHABET_SIZE = 26;
  bool is_end_of_word;

public:
  TrieNode() 
    : is_end_of_word{ false }, children{ new TrieNode*[ALPHABET_SIZE] }
  {}

  bool containsKey(char ch) {
    return children[ch - 'a'] != nullptr;
  }
  TrieNode* get(char ch) {
    return children[ch - 'a'];
  }
  void put(char ch, TrieNode* node) {
    children[ch - 'a'] = node;
  }
  void set_end() {
    is_end_of_word = true;
  }
  bool is_end() {
    return is_end_of_word;
  }
};

class Trie {
private:
  TrieNode* root;

  TrieNode* search_prefix(const std::string& word) {
    TrieNode* curr = root;
    for (char c : word) {
      if (curr->containsKey(c)) curr = curr->get(c);
      else return nullptr;
    }
    return curr;
  }

public:
  Trie() 
    : root{ new TrieNode() }
  {}

  void insert(const std::string& word) {
    TrieNode* curr = root;
    for (char c : word) {
      if (!curr->containsKey(c)) curr->put(c, new TrieNode());
      curr = curr->get(c);
    }
    curr->set_end();
  }

  bool search(const std::string& word) {
    TrieNode* node = search_prefix(word);
    return node && node->is_end();
  }
};
