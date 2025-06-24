let cart = []; // Array para armazenar os itens no carrinho

// Função para carregar e exibir os produtos
async function loadProducts() {
    try {
        const response = await fetch('/products'); // Chamada para o endpoint REST
        const products = await response.json();
        const productsContainer = document.getElementById('products-container');
        productsContainer.innerHTML = ''; // Limpa o container antes de adicionar os produtos

        products.forEach(prod => {
            const productCard = document.createElement('div');
            productCard.className = 'product-card';
            productCard.innerHTML = `
                <img src="${prod.imageUrl}" alt="${prod.nome}">
                <h3>${prod.nome}</h3>
                <p>${prod.descricao}</p>
                <p class="price">R$ ${prod.preco.toFixed(2)}</p>
                <p>Estoque: ${prod.estoqueQuantidade}</p>
                <button onclick="addToCart(${prod.id}, '${prod.nome}')" class="add-to-cart-button">Adicionar ao Carrinho</button>
            `;
            productsContainer.appendChild(productCard);
        });
    } catch (error) {
        console.error('Erro ao carregar produtos:', error);
        alert('Erro ao carregar produtos. Verifique o console.');
    }
}

// Função para adicionar um produto ao carrinho
function addToCart(productId, productName) {
    const existingItem = cart.find(item => item.productId === productId);

    if (existingItem) {
        existingItem.quantity += 1;
    } else {
        cart.push({ productId: productId, quantity: 1, name: productName });
    }
    updateCartDisplay();
    alert(`"${productName}" adicionado ao carrinho!`);
}

// Função para atualizar a exibição do carrinho
function updateCartDisplay() {
    const cartItemsList = document.getElementById('cart-items');
    cartItemsList.innerHTML = '';
    let totalQuantity = 0;

    cart.forEach(item => {
        const li = document.createElement('li');
        li.textContent = `${item.name} (x${item.quantity})`;
        cartItemsList.appendChild(li);
        totalQuantity += item.quantity;
    });

    document.getElementById('cart-total-quantity').textContent = totalQuantity;
}

// Função para finalizar a compra
async function checkout() {
    if (cart.length === 0) {
        alert('Seu carrinho está vazio!');
        return;
    }

    const orderRequest = {
        items: cart.map(item => ({
            productId: item.productId,
            quantity: item.quantity
        }))
    };

    try {
        const response = await fetch('/orders', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(orderRequest),
        });

        const result = await response.text(); // A resposta do backend é uma string
        alert(result); // Exibe a mensagem de sucesso ou erro do backend

        // Limpa o carrinho e recarrega os produtos após a compra
        cart = [];
        updateCartDisplay();
        loadProducts(); // Recarrega para refletir mudanças no estoque
    } catch (error) {
        console.error('Erro ao finalizar o pedido:', error);
        alert('Erro ao finalizar o pedido. Verifique o console.');
    }
}

// Carregar produtos quando a página for carregada
document.addEventListener('DOMContentLoaded', loadProducts);