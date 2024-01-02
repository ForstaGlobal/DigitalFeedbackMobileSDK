module.exports = {
    root: true,
    parser: "@babel/eslint-parser",
    env: {
        browser: true,
        commonjs: true,
        es6: true,
        node: true
    },
    extends: [
        'eslint:recommended',
        'plugin:@typescript-eslint/recommended',
        'prettier',
        'plugin:import/recommended',
        'plugin:import/typescript',
        "@react-native-community"
    ],
    parserOptions: {
        sourceType: 'module',
        project: ['./tsconfig.json']
    },
    plugins: [
        '@typescript-eslint',
        'import',
        'prettier',
        'react',
        "react-native"
    ],
    globals: {
        describe: "readonly",
        it: "readonly"
    },
    settings: {
        'import/resolver': {
            typescript: true,
            node: true
        },
    },
    ignorePatterns: ["*.js", "*.svg", "node_modules","mock-data","lib"],
    rules: {
        'prettier/prettier': 'error',
        '@typescript-eslint/explicit-function-return-type': 'off',
        '@typescript-eslint/no-array-constructor': 'off',
        '@typescript-eslint/explicit-member-accessibility': ['error'],
        '@typescript-eslint/no-empty-interface': 'off',
        '@typescript-eslint/strict-boolean-expressions': 'off',
        '@typescript-eslint/no-var-requires': 'off',
        '@typescript-eslint/prefer-for-of': ['error'],
        '@typescript-eslint/no-use-before-define': ['error', {  "functions": true, "classes": true, "variables": false }],
        '@typescript-eslint/no-explicit-any': 'off',
        '@typescript-eslint/camelcase': 'off',
        '@typescript-eslint/ban-ts-ignore': 'off',
        '@typescript-eslint/explicit-module-boundary-types': 'off',
        '@typescript-eslint/no-unused-vars': 'off',
        'curly': ['error'],
        'prefer-const': ['error'],
        'no-console': ['error'],
        'sort-keys': 'off',
        'camelcase': 'off',
        'no-underscore-dangle': 'off',
        'comma-dangle': 'off',
        'max-classes-per-file': 'off',
        'prefer-template': ['error'],
        'no-fallthrough': 'off',
        'sort-imports': [
            'error',
            {
                ignoreCase: false,
                ignoreDeclarationSort: true,
                ignoreMemberSort: false,
                memberSyntaxSortOrder: ['none', 'all', 'multiple', 'single'],
                allowSeparatedGroups: true,
            },
        ],
        'import/no-unresolved': 'error',
        'import/order': [
            'error',
            {
                groups: [
                    'builtin',
                    'external',
                    'internal',
                    ['sibling', 'parent'],
                    'index',
                    'unknown',
                ],
                'newlines-between': 'never',
                alphabetize: {
                    order: 'asc',
                    caseInsensitive: true,
                }
            }
        ]
    }
};
