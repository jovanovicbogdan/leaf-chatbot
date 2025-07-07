const BASE_API_URL = 'http://localhost:8080/api'
const API_V1 = '/v1';

const conversationId = self.crypto.randomUUID();

function updateConversationUserUi(conv) {
  const output = document.getElementById('output');
  const divElement = document.createElement('div');
  divElement.textContent = `${conv.type.toUpperCase()}: ${conv.message}`;
  output.appendChild(divElement);
}

function updateConversationAssistantUi(conv) {
  const output = document.getElementById('output');
  const div = document.getElementById(conv.messageId);
  if (div) {
    div.textContent += conv.message;
  } else {
    const divElement = document.createElement('div');
    divElement.id = conv.messageId;
    divElement.textContent = `${conv.type.toUpperCase()}: ${conv.message}`;
    output.appendChild(divElement);
  }
}

async function runConversation() {
  const inputElement = document.getElementById('query');
  const query = inputElement.value;
  if (!query) {
    alert('A question cannot be empty.');
    return;
  }
  inputElement.value = '';
  updateConversationUserUi({
    type: 'user',
    message: query
  });
  const response = await fetch(`${BASE_API_URL + API_V1}/conversation`, {
    method: 'POST',
    body: JSON.stringify({
      conversationId,
      query,
    }),
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'text/event-stream'
    }
  });
  const assistantMessageId = self.crypto.randomUUID();
  for await (const chunk of response.body) {
    const utf8Decoder = new TextDecoder();
    const decodedText = utf8Decoder.decode(chunk)
    .replaceAll('\n', '')
    .replaceAll('data:', '');
    updateConversationAssistantUi({
      messageId: assistantMessageId,
      type: 'assistant',
      message: decodedText
    });
  }
}

function handleKeyDown(event) {
  if (event.key.toLowerCase() === 'enter') {
    runConversation();
  }
}
