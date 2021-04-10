const container = document.getElementById('container');
const loading = document.querySelector('.loading');
const size = 3;

var start = 0;

getPost();

function infniteScroll() {
    window.addEventListener('scroll', iscroll);
}

function showLoading() {
    window.removeEventListener('scroll', iscroll);
    loading.classList.add('show');
    setTimeout(getPost, 1000)
}

function iscroll() {
    const {scrollTop, scrollHeight, clientHeight} = document.documentElement;

    console.log({scrollTop, scrollHeight, clientHeight});

    if (clientHeight + scrollTop >= scrollHeight - 5) {
        showLoading();
    }
}

async function getPost() {

    const postResponse = await fetch(`/orderdetail/${start}/${size}`);
    const data = await postResponse.json();

    start = data[size-1].orderdetailId;

    infniteScroll();
    addDataToDOM(data);
}

function addDataToDOM(data) {
    for (var i = 0; i < data.length; i++) {

        const postElement = document.createElement('div');

        postElement.classList.add('orders');
        postElement.innerHTML = `
		<h2 class="title">${data[i].orderdetailId}</h2>
		<p class="text">Order id: ${data[i].orderId} | Order line: ${data[i].orderLineNumber}</p>		
                <div class="user-info">
                    <span>Product id: ${data[i].productId}</span>
                </div>        
                <div class="user-info">
                    <span>Price: $${data[i].priceEach}</span>
                </div>		
                <div class="user-info">
                    <span>Quantity ordered: ${data[i].quantityOrdered}</span>
                </div>		 
	`;

        container.appendChild(postElement);
        loading.classList.remove('show');
    }
}