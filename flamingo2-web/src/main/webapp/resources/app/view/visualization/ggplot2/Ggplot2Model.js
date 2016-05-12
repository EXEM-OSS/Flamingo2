/*
 * Copyright (C) 2011 Flamingo Project (http://www.cloudine.io).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
Ext.define('Flamingo2.view.visualization.ggplot2.Ggplot2Model', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.ggplot2Model',

    data: {
        title: message.msg('visual.title'),
        isGeomMenuShow: false,
        "Defaults": [
            {
                "name": "x",
                "map": true,
                "required": true
            },
            {
                "name": "y",
                "map": true,
                "required": true
            },
            {
                "name": "weight",
                "map": true,
                "required": false
            },
            "-",
            {
                "name": "group",
                "map": true,
                "required": false
            },
            {
                "name": "colour",
                "map": true,
                "required": false
            },
            "-"
        ],
        "Facet": [
            {
                "name": "map",
                "map": true,
                "set": false,
                "required": false
            },
            {
                "name": "nrow",
                "map": false,
                "set": true,
                "required": false,
                "values": [
                    1,
                    2,
                    3,
                    4,
                    5,
                    6,
                    7,
                    8,
                    9,
                    10
                ]
            },
            {
                "name": "scales",
                "map": false,
                "set": true,
                "required": false,
                "values": [
                    "fixed",
                    "free",
                    "free_x",
                    "free_y"
                ]
            }
        ],
        "Aesthetics": {
            "stat": {
                "name": "stat",
                "type": "string",
                "required": false,
                "map": false,
                "set": true,
                "custom": false,
                "values": [
                    "identity"
                ]
            },
            "alpha": {
                "name": "alpha",
                "type": "numeric",
                "required": false,
                "map": true,
                "set": true,
                "custom": true,
                "values": [
                    0.1,
                    0.2,
                    0.3,
                    0.4,
                    0.5,
                    0.6,
                    0.7,
                    0.8,
                    0.9
                ]
            },
            "size": {
                "name": "size",
                "type": "numeric",
                "required": false,
                "map": true,
                "set": true,
                "custom": true,
                "values": [
                    0.1,
                    0.2,
                    0.3,
                    0.4,
                    0.5,
                    0.75,
                    1,
                    1.5,
                    2,
                    3,
                    4,
                    5
                ]
            },
            "colour": {
                "name": "colour",
                "type": "string",
                "required": false,
                "map": true,
                "set": true,
                "custom": true,
                "values": []
            },
            "fill": {
                "name": "fill",
                "type": "string",
                "required": false,
                "map": true,
                "set": true,
                "custom": true,
                "values": []
            },
            "linetype": {
                "name": "linetype",
                "type": "string",
                "required": false,
                "map": true,
                "set": true,
                "custom": true,
                "values": [
                    "solid",
                    "dashed",
                    "dotted",
                    "dotdash",
                    "longdash",
                    "twodash"
                ]
            },
            "weight": {
                "name": "weight",
                "type": "numeric",
                "required": false,
                "map": true,
                "set": false,
                "custom": false,
                "values": [
                    1
                ]
            },
            "z": {
                "name": "z",
                "type": "numeric",
                "required": true,
                "map": true,
                "set": false,
                "custom": false,
                "values": []
            },
            "shape": {
                "name": "shape",
                "type": "numeric",
                "required": false,
                "map": true,
                "set": true,
                "custom": true,
                "values": [
                    1,
                    2,
                    3,
                    4,
                    5,
                    6,
                    7,
                    8,
                    9,
                    10,
                    11,
                    12,
                    13,
                    14,
                    15
                ]
            },
            "method": {
                "name": "method",
                "type": "string",
                "required": false,
                "map": false,
                "set": true,
                "custom": false,
                "values": [
                    "lm",
                    "glm",
                    "loess"
                ]
            },
            "se": {
                "name": "se",
                "type": "boolean",
                "required": false,
                "map": false,
                "set": true,
                "custom": false,
                "values": [
                    "TRUE",
                    "FALSE"
                ]
            },
            "kernel": {
                "name": "kernel",
                "type": "string",
                "required": false,
                "map": false,
                "set": true,
                "custom": false,
                "values": [
                    "gaussian",
                    "rectangular",
                    "triangular",
                    "cosine",
                    "biweight",
                    "epanechnikov",
                    "optcosine"
                ]
            },
            "bins": {
                "name": "bins",
                "type": "numeric",
                "required": false,
                "map": false,
                "set": true,
                "custom": true,
                "values": [
                    10,
                    15,
                    20,
                    30,
                    40,
                    50,
                    100
                ]
            },
            "label": {
                "name": "label",
                "type": "string",
                "required": true,
                "map": true,
                "set": false,
                "custom": false,
                "values": []
            },
            "angle": {
                "name": "angle",
                "type": "numeric",
                "required": false,
                "map": false,
                "set": true,
                "custom": true,
                "values": [
                    45,
                    90,
                    180,
                    270
                ]
            },
            "position": {
                "name": "position",
                "type": "string",
                "required": false,
                "map": false,
                "set": true,
                "custom": false,
                "values": [
                    "dodge",
                    "fill",
                    "identity",
                    "jitter",
                    "stack"
                ]
            },
            "direction": {
                "name": "direction",
                "type": "string",
                "required": false,
                "map": false,
                "set": true,
                "custom": false,
                "values": [
                    "hv",
                    "vh"
                ]
            },
            "number": {
                "name": "number",
                "type": "numeric",
                "required": false,
                "map": false,
                "set": true,
                "custom": true,
                "values": [
                    2,
                    3,
                    4,
                    5,
                    6,
                    7,
                    8,
                    9
                ]
            },
            "adjust": {
                "name": "adjust",
                "type": "numeric",
                "required": false,
                "map": false,
                "set": true,
                "custom": true,
                "values": [
                    0.05,
                    0.1,
                    0.2,
                    0.5,
                    0.75,
                    1,
                    1.25,
                    1.5,
                    2,
                    3,
                    4,
                    5,
                    10,
                    20
                ]
            },
            "intercept": {
                "name": "intercept",
                "type": "numeric",
                "required": true,
                "map": true,
                "set": true,
                "custom": true,
                "values": [
                    1
                ]
            },
            "slope": {
                "name": "slope",
                "type": "numeric",
                "required": true,
                "map": true,
                "set": true,
                "custom": true,
                "values": [
                    1
                ]
            },
            "xintercept": {
                "name": "xintercept",
                "type": "numeric",
                "required": true,
                "map": true,
                "set": true,
                "custom": true,
                "values": [
                    1
                ]
            },
            "yintercept": {
                "name": "yintercept",
                "type": "numeric",
                "required": true,
                "map": true,
                "set": true,
                "custom": true,
                "values": [
                    1
                ]
            },
            "ymin": {
                "name": "ymin",
                "type": "numeric",
                "required": true,
                "map": true,
                "set": false,
                "custom": false,
                "values": []
            },
            "ymax": {
                "name": "ymax",
                "type": "numeric",
                "required": true,
                "map": true,
                "set": false,
                "custom": false,
                "values": []
            },
            "xmin": {
                "name": "xmin",
                "type": "numeric",
                "required": true,
                "map": true,
                "set": false,
                "custom": false,
                "values": []
            },
            "xmax": {
                "name": "xmax",
                "type": "numeric",
                "required": true,
                "map": true,
                "set": false,
                "custom": false,
                "values": []
            },
            "width": {
                "name": "width",
                "type": "numeric",
                "required": false,
                "map": false,
                "set": true,
                "custom": true,
                "values": [
                    1
                ]
            },
            "binwidth": {
                "name": "binwidth",
                "type": "numeric",
                "required": false,
                "map": false,
                "set": true,
                "custom": true,
                "values": []
            }
        },
        "Required": {
            "abline": [
                "intercept",
                "slope"
            ],
            "text": [
                "label"
            ],
            "contour": [
                "z"
            ],
            "tile": [
                "fill"
            ],
            "hline": [
                "yintercept"
            ],
            "vline": [
                "xintercept"
            ],
            "crossbar": [
                "ymin",
                "ymax"
            ],
            "errorbar": [
                "ymin",
                "ymax"
            ],
            "linerange": [
                "ymin",
                "ymax"
            ],
            "pointrange": [
                "ymin",
                "ymax"
            ],
            "ribbon": [
                "ymin",
                "ymax"
            ],
            "errorbarh": [
                "xmin",
                "xmax"
            ]
        },
        "Statvar": {
            "contour": [
                "..level..",
                "..piece.."
            ],
            "density2d": [
                "..level..",
                "..piece.."
            ],
            "hex": [
                "..count.."
            ],
            "bin2d": [
                "..count.."
            ],
            "quantile": [
                "..quantile.."
            ]
        }
    },
    stores: {
        dataList: {
            autoLoad: false,
            model: 'Flamingo2.model.visualization.dataList'
        },
        generalOptions: {
            fields: ['name', 'value']
        },
        layerOptions: {
            fields: ['name', 'value']
        }
    }
});