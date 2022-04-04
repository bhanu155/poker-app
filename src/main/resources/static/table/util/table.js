sap.ui.define([], () => {
  const API_PATH = '/api/v1'

  const _fetch = async (path, options) => {
    const response = await fetch(path, options)
    if (!response.ok) {
      const message = await response.text()
      throw new Error(message)
    }
    return response
  }

  return {
    async fetch() {
      const response = await _fetch(API_PATH)
      const { currentPlayer, players, communityCards, playerCards, bets, pot } = await response.json()
      return {
        currentPlayer,
        players,
        communityCards,
        playerCards,
        bets,
        pot
      }
    },

    async join() {
      await _fetch(`${API_PATH}/players`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        }
      })
    },

    async action(type, ...args) {
      await _fetch(`${API_PATH}/actions`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          type,
          args
        })
      })
    }
  }
})
