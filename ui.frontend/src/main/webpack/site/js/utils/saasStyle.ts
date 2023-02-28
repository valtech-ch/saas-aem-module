const initSaasStyle = (): void => {
    const style = document.createElement("style");
    document.head.prepend(style); // must append before we can access sheet property
    const sheet = style.sheet;

    if (sheet) {
        sheet.insertRule(
            `
            #cmp-saas-suggestions {
              position: absolute;
              border: 1px solid #d4d4d4;
              border-bottom: none;
              border-top: none;
              z-index: 99;
              top: 100%;
              left: 0;
              right: 0;
            }
          `,
            0,
        )

        sheet.insertRule(
            `
            .cmp-saas__search-input-wrapper {
               position: relative;
               display: inline-block;
            }
          `,
            0,
        )

        sheet.insertRule(
            `
            .cmp-saas__suggestion-element {
              padding: 10px;
              cursor: pointer;
              background: #fff;
            }
          `,
            0,
        )

        sheet.insertRule(
            `
            .cmp-saas__suggestion-element:last-child {
              border-bottom: 1px solid #d4d4d4;
            }
          `,
            0,
        )

        sheet.insertRule(
            `
            .cmp-saas__suggestion-element:hover {
              background-color: #e9e9e9;
            }
          `,
            0,
        )

        sheet.insertRule(
            `
            .cmp-saas__suggestion-element--active {
              background-color: DodgerBlue !important;
              color: #fff;
            }
          `,
            0,
        )
    }
}

export default initSaasStyle
