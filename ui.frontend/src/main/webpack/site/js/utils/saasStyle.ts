const initSaasStyle = (): void => {
  document.styleSheets[0].insertRule(
    `
            #suggestions {
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

  document.styleSheets[0].insertRule(
    `
            .cmp-saas__autocomplete{
               position: relative;
               display: inline-block;
            }
          `,
    0,
  )

  document.styleSheets[0].insertRule(
    `
            #suggestions .saas-suggestions-element {
              padding: 10px;
              cursor: pointer;
              background: #fff;
            }
          `,
    0,
  )

  document.styleSheets[0].insertRule(
    `
            #suggestions .saas-suggestions-element:last-child {
              border-bottom: 1px solid #d4d4d4;
            }
          `,
    0,
  )

  document.styleSheets[0].insertRule(
    `
            #suggestions .saas-suggestions-element:hover {
              background-color: #e9e9e9;
            }
          `,
    0,
  )

  document.styleSheets[0].insertRule(
    `
            .saas-suggestions-element--active {
              background-color: DodgerBlue !important;
              color: #fff;
            }
          `,
    0,
  )
}

export default initSaasStyle
