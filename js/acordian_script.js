const accordionItemHeaders = document.querySelectorAll(".accordion-item-header");

accordionItemHeaders.forEach(accordionItemHeader => {
  
  accordionItemHeader.addEventListener("click", event => {

    const currentlyActiveAccordionItemHeader = document.querySelector(".accordion-item-header.active");
     
    if(currentlyActiveAccordionItemHeader && currentlyActiveAccordionItemHeader!==accordionItemHeader) {
      currentlyActiveAccordionItemHeader.classList.toggle("active");
      currentlyActiveAccordionItemHeader.nextElementSibling.style.maxHeight = 0;
    }
    
    accordionItemHeader.classList.toggle("active");
    const accordionItemBody = accordionItemHeader.nextElementSibling;
    if(accordionItemHeader.classList.contains("active")) {
      accordionItemBody.style.maxHeight = accordionItemBody.scrollHeight + "px";
    }
    else {
      accordionItemBody.style.maxHeight = 0;
    }
  });
});

const navbar = document.querySelector('.custom-navbar');
let scrollTimeout;

window.addEventListener('scroll', () => {
  navbar.classList.add('custom-scrolled');
  clearTimeout(scrollTimeout);

  scrollTimeout = setTimeout(() => {
    navbar.classList.remove('custom-scrolled');
  }, 100); // 스크롤 멈춤 후 100ms 대기
});