const initSearch = () => {
    window.addEventListener('load', () => {
        const searchForm = document.querySelector<HTMLFormElement>(
            '.saas-aem-module-search__form'
        );
        const searchInput = searchForm?.querySelector<HTMLInputElement>(
            '.saas-aem-module-search__input'
        );

        searchForm?.addEventListener('submit', (event) => {
            event?.preventDefault();

            const searchInputValue = searchInput?.value;

            console.log(searchInputValue);
        });
    });
};

export default initSearch;
