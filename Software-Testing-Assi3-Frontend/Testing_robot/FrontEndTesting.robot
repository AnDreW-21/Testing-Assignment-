*** Settings ***
Library           SeleniumLibrary
Library           Collections
Library           String

*** Variables ***
${sleep}          2
${To-Do}          assignment Software
${Description}    assignment-3
${expected_value}    on

*** Test Cases ***
TastCase-1
    [Setup]    Open Browser    http://127.0.0.1:5500/todo.html    Chrome
    Maximize Browser Window
    Wait Until Page Contains Element    id=todo-form
    Wait Until Page Contains Element    id=todo-form
    Sleep    ${sleep}
    Input Text    id=todo    ${To-Do}
    Input Text    id=desc    ${Description}
    Click Element    xpath=//button[@type='submit']
    Sleep    ${sleep}
    Wait Until Element Is Visible    //*[@id="todo-table"]
    Element Text Should Be    id=todo    ${EMPTY}
    Element Text Should Be    id=desc    ${EMPTY}
    ${last_title}    Get WebElement    //tr[last()]/td[2]
    ${last_des}    Get WebElement    //tr[last()]/td[3]
    log    ${last_title.text}
    Log    ${last_des.text}
    Should Be Equal    ${To-Do}    ${last_title.text}
    Should Be Equal    ${Description}    ${last_des.text}
    [Teardown]    Close Browser

TestCase-2
    [Setup]    Open Browser    http://127.0.0.1:5500/todo.html    Chrome
    Maximize Browser Window
    Sleep    ${sleep}
    Wait Until Element Is Visible    //*[@id="todo-table"]
    ${checkbox}    Get WebElement    css=#todo-table input[type="checkbox"]:first-child
    ${is_checked}    Get Element Attribute    ${checkbox}    checked
    Log    ${is_checked}
    Click Element    ${checkbox}
    Sleep    ${sleep}
    ${checkbox1}    Get WebElement    css=#todo-table input[type="checkbox"]:first-child
    ${checkbox_value1}    Get Element Attribute    ${checkbox1}    checked
    Log    Checkbox Value: ${checkbox_value1}
    Should Not Be Equal    ${checkbox_value1}    ${is_checked}
    Log    ${is_checked}
    Log    ${checkbox_value1}
    ${columns}=    Get WebElements    css=#todo-table tr td:nth-child(1)
    ${length}=    Get Length    ${columns}
    Log    ${length}
    ${delete_button}    Get WebElement    css=#todo-table tr:first-child td:last-child button
    Click Element    ${delete_button}
    ${columns}=    Get WebElements    css=#todo-table tr td:nth-child(1)
    ${length-updated}=    Get Length    ${columns}
    Should Be True    ${length}>${length-updated}
    [Teardown]    Close Browser

TestCase-3
    [Setup]    Open Browser    http://127.0.0.1:5500/todo.html    Chrome
    Maximize Browser Window
    Sleep    ${sleep}
    Input Text    id=todo    ${To-Do}
    Input Text    id=desc    ${Description}
    Click Element    xpath=//button[@type='submit']
    Click Element    css=body > div > div > div.col-4 > button:nth-child(3)
    Sleep    ${sleep}
    ${checkboxes}    Get WebElements    css=#todo-table tr td:nth-child(4) input
    ${length}=    Get Length    ${checkboxes}
    Log    ${length}
    FOR    ${checkbox}    IN    @{checkboxes}
        ${is_checked}    Call Method    ${checkbox}    is_selected
        Log    Checkbox State: ${is_checked}
        Should Be True    ${is_checked}
    END
    Click Element    css=body > div > div > div.col-4 > button:nth-child(2)
    Wait Until Element Is Visible    //*[@id="todo-table"]
    Sleep    ${sleep}
    ${checkboxes1}    Get WebElements    css=#todo-table tr td:nth-child(4) input
    ${nLength}=    Get Length    ${checkboxes1}
    Should Be True    ${nLength}>=${length}
    Log    ${nLength}
    [Teardown]    Close Browser
