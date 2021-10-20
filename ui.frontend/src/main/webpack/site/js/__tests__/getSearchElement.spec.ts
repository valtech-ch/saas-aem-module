import getSearchElement from '../getSearchElement';

describe('getSearchElement', () => {
    test('returns null when the element is not on the page', () => {
        document.body.innerHTML = `
            <html lang="en">
                <body>
                    <div></div>
                </body>
            </html>
        `;

        expect(getSearchElement()).toBe(null);
    });

    test('returns the search element', () => {
        document.body.innerHTML = `
            <html lang="en">
                <body>
                    <search></search>
                </body>
            </html>
        `;

        expect(getSearchElement()).toBeTruthy();
    });
});
